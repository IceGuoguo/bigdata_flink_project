package com.baizhi.evaluate;

import com.baizhi.model.EvalauteData;
import com.baizhi.model.EvaluateReport;
import com.baizhi.model.HistoryData;

import java.util.Set;

public class RegionEvalaute extends Evaluate {

    public RegionEvalaute(String type) {
        super(type);
    }

    @Override
    public void evalue(EvalauteData evalauteData, HistoryData historyData, EvaluateReport evaluateReport,
                       EvaluateChain chain) {
        evaluateReport.addReport(this.type,eval(evalauteData.getCity(),evalauteData.getLatitude(),
                evalauteData.getLongtitude(),
                evalauteData.getTime(),
                historyData.getHistoryCity(),
                historyData.getLatitude(),
                historyData.getLongitude(),
                historyData.getLastLoginDate()));
        chain.evaluate(evalauteData,historyData,evaluateReport);
    }

    /**
     *
     * @param currentCity
     * @param cw
     * @param cj
     * @param historyCities
     * @param hw
     * @param hj
     * @return
     */
    private boolean eval(String currentCity, double cw, double cj, long cdate,
                        Set<String> historyCities, double hw, double hj,long ldate){

        //是否是新的地区
        if(historyCities!=null && !historyCities.contains(currentCity)){
            return true;
        }

        double hours=(cdate-ldate)*1.0/(3600*1000);//当前时差
        double km = distance(cw, cj, hw, hj);
        //计算用户平均速度
        double speed=km/hours;
        System.out.println(speed);
        if(speed>350){
            return true;
        }
        return false;
    }
    private double distance(double cw, double cj,double hw, double hj){
         cw = Math.toRadians(cw);
         cj = Math.toRadians(cj);
         hw = Math.toRadians(hw);
         hj = Math.toRadians(hj);
         return Math.acos(Math.cos(cw)*Math.cos(hw)*Math.cos(cj-hj)+Math.sin(cw)*Math.sin(hw)) * 6371;
    }
}
