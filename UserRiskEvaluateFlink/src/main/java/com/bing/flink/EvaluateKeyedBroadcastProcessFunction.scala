package com.bing.flink

import java.util
import java.util.Arrays

import com.bing.evaluate.{DeviceEvalaute, Evaluate, EvaluateChain, HabitEvalaute, InputEvalaute, PassowdEvalaute, RegionEvalaute}
import com.bing.model.{EvalauteData, EvaluateReport, HistoryData, LoginSuccessData, RiskType}
import org.apache.flink.api.common.state.MapStateDescriptor
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.co.KeyedBroadcastProcessFunction
import org.apache.flink.util.Collector

class EvaluateKeyedBroadcastProcessFunction(msd:MapStateDescriptor[String,HistoryData]) extends KeyedBroadcastProcessFunction[String,(String,EvalauteData),(String,HistoryData),(String,EvaluateReport)]{
  var evalutes:util.List[Evaluate]=_

  override def processElement(value: (String, EvalauteData), ctx: KeyedBroadcastProcessFunction[String, (String, EvalauteData), (String, HistoryData), (String,EvaluateReport)]#ReadOnlyContext, out: Collector[(String,EvaluateReport)]): Unit = {
    val historyDataSate = ctx.getBroadcastState(msd)
    val historyData = historyDataSate.get("historyBroadCast")

    val evaluateChain = new EvaluateChain(evalutes)
    val report = new EvaluateReport()
    evaluateChain.evaluate(value._2,historyData,report)
    out.collect((value._1,report))
  }

  override def processBroadcastElement(value: (String, HistoryData), ctx: KeyedBroadcastProcessFunction[String, (String, EvalauteData), (String, HistoryData), (String,EvaluateReport)]#Context, out: Collector[(String,EvaluateReport)]): Unit = {
    ctx.getBroadcastState(msd).put("historyBroadCast",value._2)
  }

  override def open(parameters: Configuration): Unit = {
    evalutes = util.Arrays.asList(new HabitEvalaute(RiskType.LOGIN_HABIT), new DeviceEvalaute(RiskType.LOGIN_DEVICE), new PassowdEvalaute(RiskType.INPUT_PASWORD), new RegionEvalaute(RiskType.LOGIN_REGION), new InputEvalaute(RiskType.INPUTS_FUTURE))
  }
}
