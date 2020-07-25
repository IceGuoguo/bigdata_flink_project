package com.bing.flink

import com.bing.model.{HistoryData, LoginSuccessData}
import com.bing.utils.HistoryDataUitl
import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.api.common.state.{ValueState, ValueStateDescriptor}
import org.apache.flink.configuration.Configuration
import org.apache.flink.api.scala._

class UserLoginRichMapFunction extends RichMapFunction[(String,LoginSuccessData),(String,HistoryData)]{

  var histriyDataState:ValueState[HistoryData]=_

  override def map(value: (String, LoginSuccessData)): (String, HistoryData) = {
    var historyData:HistoryData=null
    if(histriyDataState.value()==null){
      historyData=new HistoryData();
    }else{
      historyData=histriyDataState.value()
    }
    HistoryDataUitl.updateHistoryData(value._2,historyData)
    //存储状态
    histriyDataState.update(historyData)
    (value._1,historyData)
  }

  override def open(parameters: Configuration): Unit = {
    val historyDataDescriptor=new ValueStateDescriptor[HistoryData]("historyData",createTypeInformation[HistoryData])
    histriyDataState=getRuntimeContext.getState(historyDataDescriptor)
  }
}
