package com.baizhi.spark

import com.baizhi.model.{HistoryData, LoginSuccessData}
import com.baizhi.utils.{HistoryDataUitl, UserLoginLogParser}
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, State, StateSpec, StreamingContext}

object UserLoginHistoryTopology {
  def main(args: Array[String]): Unit = {
    var checkpointDir="file:///D:/checkpoints"
    //1.构建ssc对象
    val ssc=StreamingContext.getOrCreate(checkpointDir,()=>{
    val conf = new SparkConf()
                  .setAppName("UserLoginHistoryTopology")
                  .setMaster("local[6]")

    val ssc = new StreamingContext(conf,Seconds(1))
    ssc.checkpoint(checkpointDir)

    //从外围读取登陆成功数据
    ssc.socketTextStream("CentOS",9999)
        .filter(line=>UserLoginLogParser.isLegal(line))
        .map(line=>UserLoginLogParser.parseLoginSuccessData(line))
        .map(loginSuccessData=>(loginSuccessData.getUsername+":"+loginSuccessData.getAppName,loginSuccessData))
         //实现数据评估
        .mapWithState(StateSpec.function((key:String,value:Option[LoginSuccessData],state:State[HistoryData])=>{
            var historyData:HistoryData=null
            if(state.exists()){
               historyData = state.get()
            }else{
              historyData=new HistoryData();
            }
            var loginSuccessData=value.getOrElse(null)
            HistoryDataUitl.updateHistoryData(loginSuccessData,historyData)
            state.update(historyData)
            (key,historyData)
          })).foreachRDD(rdd=>{
            rdd.foreachPartition(tuples=>{//将评估结果写入到Redis中
              tuples.foreach(tuple=> RedisSink.writeToRedis(tuple._1,tuple._2))
            })
        })
     ssc
    })
    ssc.sparkContext.setLogLevel("FATAL")
    ssc.start()
    ssc.awaitTermination()

  }
}
