package com.baizhi.evaluate;

import com.baizhi.model.EvalauteData;
import com.baizhi.model.EvaluateReport;
import com.baizhi.model.HistoryData;
import com.baizhi.model.RiskType;
import com.baizhi.utils.UserLoginLogParser;

import java.text.ParseException;
import java.util.*;

public class EvaluateChain {
    private List<Evaluate> evaluates;
    private Integer pos=0;//记录评估位置
    private Integer size;//记录所有的评估大小

    public EvaluateChain(List<Evaluate> evaluates) {
        this.evaluates = evaluates;
        this.size=evaluates.size();
    }

    public void evaluate(EvalauteData evalauteData,
                         HistoryData historyData,
                         EvaluateReport evaluateReport){
        if(historyData==null) {
            return;
        };
        if(pos<size){
            pos++;
            Evaluate evaluate=evaluates.get(pos-1);
            evaluate.evalue(evalauteData,historyData,evaluateReport,this);
        }else{
            return;
        }
    }

    public static void main(String[] args) throws ParseException {
        EvaluateChain evalauteChain = new EvaluateChain(Arrays.asList(
                new HabitEvalaute(RiskType.LOGIN_HABIT),
                new DeviceEvalaute(RiskType.LOGIN_DEVICE),
                new PassowdEvalaute(RiskType.INPUT_PASWORD),
                new RegionEvalaute(RiskType.LOGIN_REGION),
                new InputEvalaute(RiskType.INPUTS_FUTURE)
        ));
        EvaluateReport evaluateReport = new EvaluateReport();
        EvalauteData evalauteData=buildEvalauteData();
        HistoryData historyData=buildHistoryData();

        evalauteChain.evaluate(evalauteData,historyData,evaluateReport);

        for (Map.Entry<String, Boolean> entry : evaluateReport.getRepoert().entrySet()) {
            System.out.println(entry.getKey()+"\t"+entry.getValue());
        }

    }
    public static EvalauteData buildEvalauteData() throws ParseException {
        String log="INFO com.baizhi.controller.UserController#userLogin 2019-09-11 16:24:40 evaluate aap1 zhangsan 123456 成都 104.06,30.67 758,2228,1743 [Mobile Safari Browser (mobile) 11.0 APPLE iOS 11 (iPhone)]";
        return UserLoginLogParser.parseEvalauteData(log);
    }
    public static HistoryData buildHistoryData(){
        HistoryData historyData = new HistoryData();
        //登陆习惯模拟
        HashMap<String, String> loginHabits = new HashMap<>();
        loginHabits.put("星期二","11:3,12:3,15:4,18:5,20:1,22:1");
        historyData.setLoginHabits(loginHabits);

        //设备模拟
        historyData.setLastAgent("Mobile Safari Browser (mobile) 11.0 APPLE iOS 12 (iPhone)");
        //历史密码模拟
        HashSet<String> pwds = new HashSet<>();
        pwds.add("123abc");
        historyData.setHistoryPasswords(pwds);

        //登陆地区模拟
        HashSet<String> cities = new HashSet<>();
        cities.add("北京");
        cities.add("成都");
        cities.add("郑州");
        cities.add("西安市");
        historyData.setHistoryCity(cities);
        historyData.setLastLoginDate(new Date().getTime() - 1800*1000);
        historyData.setLongitude(113.62);
        historyData.setLatitude(34.75);
        //模拟输入习惯
        ArrayList<Integer[]> inputFutures = new ArrayList<>();
        inputFutures.add(new Integer[]{758,2328,1843});
        inputFutures.add(new Integer[]{800,2528,1743});
        inputFutures.add(new Integer[]{908,2128,2043});
        inputFutures.add(new Integer[]{950,2428,1700});
        historyData.setHistoryInputFutures(inputFutures);

        return historyData;
    }
}
