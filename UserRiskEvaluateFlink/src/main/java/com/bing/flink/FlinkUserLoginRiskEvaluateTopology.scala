package com.bing.flink

import java.util.Properties
import java.util.regex.Pattern

import com.bing.model.{EvaluateReport, HistoryData, LoginSuccessData}
import com.bing.utils.UserLoginLogParser
import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.api.common.state.MapStateDescriptor
import org.apache.flink.streaming.api.functions.ProcessFunction
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer, FlinkKafkaProducer}
import org.apache.flink.streaming.util.serialization.SimpleStringSchema
import org.apache.flink.util.Collector
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
object FlinkUserLoginRiskEvaluateTopology {
  def main(args: Array[String]): Unit = {
    //1.构建Flink执行环境
    val fsEnv = StreamExecutionEnvironment.getExecutionEnvironment

    val props=new Properties()
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"CentOS:9092")
    props.put(ConsumerConfig.GROUP_ID_CONFIG,"g1")

    val props2=new Properties()
    props2.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"CentOS:9092")

    val kafkaConsumer=new FlinkKafkaConsumer("user_risk",new SimpleStringSchema(),props)
    val kafkaProducer=new FlinkKafkaProducer[(String,EvaluateReport)]("evaluate_result_default",new CustomKeyedSerializationSchema,props2)

    val successTag = new OutputTag[String]("success")
    //2.设置Source
    val evaluteStream = fsEnv.addSource(kafkaConsumer)
      .filter(line => UserLoginLogParser.isLegal(line))
      .process(new ProcessFunction[String, String] {
        override def processElement(value: String, ctx: ProcessFunction[String, String]#Context, out: Collector[String]): Unit = {
          val REGEX_EXPRESS: String = "^INFO\\s.*\\s([0-9]{4}-[0-9]{2}-[0-9]{2}\\s[0-9]{2}:[0-9]{2}:[0-9]{2})\\s(evaluate|success)\\s(.*)\\s(.*)\\s(.*)\\s(.*)\\s(.*)\\s(.*)\\s\\[(.*)\\]"
          val pattern = Pattern.compile(REGEX_EXPRESS)
          val matcher = pattern.matcher(value)
          matcher.matches() //必须在group之前match
          val logType = matcher.group(2)
          if (logType.equals("success")) {
            ctx.output(successTag, value);
          } else { //评估流
            out.collect(value);
          }
        }
      })
    //构建用户历史状态state
    val msd = new MapStateDescriptor[String,HistoryData]("historyData",createTypeInformation[String],createTypeInformation[HistoryData])
    val loginSuccess=evaluteStream.getSideOutput(successTag)
     //处理用户登陆成功的数据，主要负责更新历史状态
    val broadcaststream=loginSuccess.map(line=>UserLoginLogParser.parseLoginSuccessData(line))
         .map(ls=>(ls.getUsername+":"+ls.getAppName,ls))
         .keyBy(0)
         .map(new UserLoginRichMapFunction)
         .broadcast(msd)

    evaluteStream.map(line=>UserLoginLogParser.parseEvalauteData(line))
        .map(ed=>(ed.getUsername+":"+ed.getAppName,ed))
        .keyBy(0)
        .connect(broadcaststream)
        .process(new EvaluateKeyedBroadcastProcessFunction(msd))
        .addSink(kafkaProducer)

    //6.执行流计算
    fsEnv.execute("FlinkUserLoginRiskEvaluateTopology")

  }
}
