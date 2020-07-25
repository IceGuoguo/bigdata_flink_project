package com.baizhi.spark

import java.util

import com.baizhi.evaluate.{DeviceEvalaute, Evaluate, EvaluateChain, HabitEvalaute, InputEvalaute, PassowdEvalaute, RegionEvalaute}
import com.baizhi.model.{EvaluateReport, HistoryData, LoginSuccessData, RiskType}
import com.baizhi.utils.{HistoryDataUitl, UserLoginLogParser}
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, State, StateSpec, StreamingContext}

object UserLoginEvaluateTopology {
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
    ssc.socketTextStream("CentOS",8888)
        .filter(line=>UserLoginLogParser.isLegal(line))
        .map(line=>UserLoginLogParser.parseEvalauteData(line))
        .map(evaluateData=>(evaluateData.getUsername+":"+evaluateData.getAppName,evaluateData))
        .foreachRDD(rdd=>{
          rdd.foreachPartition(tuples=>{
            var evalutes:util.List[Evaluate]=util.Arrays.asList(new HabitEvalaute(RiskType.LOGIN_HABIT), new DeviceEvalaute(RiskType.LOGIN_DEVICE), new PassowdEvalaute(RiskType.INPUT_PASWORD), new RegionEvalaute(RiskType.LOGIN_REGION), new InputEvalaute(RiskType.INPUTS_FUTURE))
            tuples.foreach(tuple=>{

              val historyData = RedisReader.readFromRedis(tuple._1)
              val evaluateData=tuple._2
              val chain = new EvaluateChain(evalutes)
              val report = new EvaluateReport
              chain.evaluate(evaluateData,historyData,report)
              println(tuple._1+"\t"+report.toString)
            })
          })
        })
     ssc
    })
    ssc.sparkContext.setLogLevel("FATAL")
    ssc.start()
    ssc.awaitTermination()

  }
}
