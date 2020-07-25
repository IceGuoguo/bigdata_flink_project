package com.bing.flink

import com.bing.model.EvaluateReport
import org.apache.flink.streaming.util.serialization.KeyedSerializationSchema

class CustomKeyedSerializationSchema extends KeyedSerializationSchema[(String,EvaluateReport)]{
  override def serializeKey(t: (String, EvaluateReport)): Array[Byte] = {
    t._1.getBytes()
  }

  override def serializeValue(t: (String, EvaluateReport)): Array[Byte] = {
    t._2.toString.getBytes()
  }
  //返回目标topic ，如果该方法返回null，则使用默认
  override def getTargetTopic(t: (String, EvaluateReport)): String = {
    "evaluate_result_"+t._1.split(":")(1)
  }
}

