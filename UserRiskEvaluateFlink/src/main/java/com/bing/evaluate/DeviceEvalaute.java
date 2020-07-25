package com.bing.evaluate;

        import com.bing.model.EvalauteData;
        import com.bing.model.EvaluateReport;
        import com.bing.model.HistoryData;

public class DeviceEvalaute extends Evaluate {

    public DeviceEvalaute(String type) {
        super(type);
    }

    @Override
    public void evalue(EvalauteData evalauteData, HistoryData historyData, EvaluateReport evaluateReport,
                       EvaluateChain chain) {
        evaluateReport.addReport(this.type,eval(evalauteData.getAgentInfo(),historyData.getLastAgent()));
        chain.evaluate(evalauteData,historyData,evaluateReport);
    }
    private boolean eval(String currentAgent,String lastAgent){
        return !currentAgent.equalsIgnoreCase(lastAgent);
    }

}
