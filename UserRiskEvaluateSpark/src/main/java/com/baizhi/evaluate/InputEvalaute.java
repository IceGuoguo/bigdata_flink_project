package com.baizhi.evaluate;

import com.baizhi.model.EvalauteData;
import com.baizhi.model.EvaluateReport;
import com.baizhi.model.HistoryData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class InputEvalaute extends Evaluate {

    public InputEvalaute(String type) {
        super(type);
    }

    @Override
    public void evalue(EvalauteData evalauteData, HistoryData historyData, EvaluateReport evaluateReport,
                       EvaluateChain chain) {

        evaluateReport.addReport(this.type,eval(evalauteData.getInputFutures(),historyData.getHistoryInputFutures()));
        chain.evaluate(evalauteData,historyData,evaluateReport);
    }

    public boolean eval(Integer[] inputFutures, List<Integer[]> historyInputFutures){

        if(historyInputFutures==null || historyInputFutures.size()<2){
            return false;
        }

        List<Double> historyDistances=new ArrayList<Double>();//n(n-1)/2
        //计算历史向量的 两两 距离
        for (int i = 0; i < historyInputFutures.size(); i++) {
            Integer[] currentInputFuture=historyInputFutures.get(i);
            //获取剩余向量
            List<Integer[]> subList = historyInputFutures.subList(i + 1, historyInputFutures.size());
            for (Integer[] input : subList) {
                historyDistances.add(distance(currentInputFuture,input));
            }
        }
        historyDistances.sort(new Comparator<Double>() {
            @Override
            public int compare(Double o1, Double o2) {
                return (o1.compareTo(o2));
            }
        });
        //取1/3作为阈值
        Integer size=historyInputFutures.size();
        Double threshold= historyDistances.get(size*(size-1)/(2*3));

        Integer[] sumVectors = historyInputFutures.stream().reduce((v1, v2) -> {
            Integer[] sum = new Integer[v1.length];
            for (int i = 0; i < v1.length; i++) {
                sum[i] = v1[i] + v2[i];
            }
            return sum;
        }).get();
        //计算平均输入特征向量
        Integer[] avgVector=new Integer[sumVectors.length];
        for (int i = 0; i < sumVectors.length; i++) {
            avgVector[i]=((Long)Math.round(sumVectors[i]*1.0/size)).intValue();
        }
        //计算当前向量和平均向量的距离
        double currentDistance = distance(inputFutures, avgVector);
        return currentDistance> threshold;
    }
    //欧式距离公式
    private double distance(Integer[] v1,Integer[] v2){
        Double total=0.0;
        for(int i=0;i<v1.length;i++){
            total+= Math.pow(v1[i]-v2[i],2);
        }
        return Math.sqrt(total);
    }
}
