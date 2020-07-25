package com.bing.evaluate;

import com.bing.model.EvalauteData;
import com.bing.model.EvaluateReport;
import com.bing.model.HistoryData;

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
