package com.baizhi.model;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class EvaluateReport implements Serializable {
    private Map<String,Boolean> report=new HashMap<>();
    public EvaluateReport(){
        addReport(RiskType.LOGIN_DEVICE,false);
        addReport(RiskType.LOGIN_REGION,false);
        addReport(RiskType.INPUT_PASWORD,false);
        addReport(RiskType.INPUTS_FUTURE,false);
        addReport(RiskType.LOGIN_HABIT,false);
    }

    public void addReport(String type, Boolean risk){
        report.put(type,risk);
    }
    public Map<String,Boolean> getRepoert(){
        return report;
    }

    @Override
    public String toString() {

        return report.entrySet().stream()
                .map(entry->entry.getKey()+":"+entry.getValue())
                .reduce((v1,v2)->v1+","+v2).get();
    }
}
