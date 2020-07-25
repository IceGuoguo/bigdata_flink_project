package com.baizhi.evaluate;

import com.baizhi.model.EvalauteData;
import com.baizhi.model.EvaluateReport;
import com.baizhi.model.HistoryData;

public abstract class Evaluate {
    protected String type;//评估类型
    public Evaluate(String type){
        this.type=type;
    }

    /**
     * @param evalauteData
     * @param historyData
     * @param evaluateReport
     * @param chain
     */
    public abstract void evalue(EvalauteData evalauteData,
                                HistoryData historyData,
                                EvaluateReport evaluateReport, EvaluateChain chain);
}
