package com.baizhi.evaluate;

import com.baizhi.model.EvalauteData;
import com.baizhi.model.EvaluateReport;
import com.baizhi.model.HistoryData;

import java.util.*;

public class PassowdEvalaute extends Evaluate {

    public PassowdEvalaute(String type) {
        super(type);
    }

    @Override
    public void evalue(EvalauteData evalauteData, HistoryData historyData, EvaluateReport evaluateReport,
                       EvaluateChain chain) {
        evaluateReport.addReport(this.type, eval(evalauteData.getPassword(),historyData.getHistoryPasswords()));
        chain.evaluate(evalauteData, historyData, evaluateReport);
    }

    public boolean eval(String password, Set<String> historyPassword) {
        StringBuilder sb = new StringBuilder();
        //将所有的数据加入到allPsswords,构建词袋模型
        List<String> allPsswords = new ArrayList<String>();
        allPsswords.add(password);
        allPsswords.addAll(historyPassword);
        allPsswords.forEach(item -> sb.append(item));

        List<Character> wordBag = new ArrayList<>();
        for (char c : sb.toString().toCharArray()) {
            if (!wordBag.contains(c)) {
                wordBag.add(c);
            }
        }
        //对wordBag排序
        wordBag.sort(new Comparator<Character>() {
            @Override
            public int compare(Character o1, Character o2) {
                return o1.compareTo(o2);
            }
        });
        //计算所有历史密码的特征向量
        List<Integer[]> hiostryVectors = new ArrayList<>();
        for (String s : historyPassword) {
            Integer[] vector = convertPassword2Vector(s, wordBag);
            System.out.println(Arrays.asList(vector) + "\t" + s);
            hiostryVectors.add(vector);
        }
        //计算当前密码特征向量
        Integer[] currentVector = convertPassword2Vector(password, wordBag);

        for (Integer[] hiostryVector : hiostryVectors) {
            Double similary = cosineSimilarity(currentVector, hiostryVector);
            System.out.println(similary);
            if(similary>0.90){//如果和历史相似度大于0.95
                return false;
            }
        }
        return true;
    }

    private Double cosineSimilarity(Integer[] v1, Integer[] v2) {
        Double sum = 0.0;
        for (int i = 0; i < v1.length; i++) {
            sum += v1[i] * v2[i];
        }
        Double v1Pow = Arrays.asList(v1).stream().map(v -> Math.pow(v, 2)).reduce((n1, n2) -> n1 + n2).get();
        Double v2Pow = Arrays.asList(v2).stream().map(v -> Math.pow(v, 2)).reduce((n1, n2) -> n1 + n2).get();
        return sum / (Math.sqrt(v1Pow) * Math.sqrt(v2Pow));
    }

    private Integer[] convertPassword2Vector(String password, List<Character> wordBag) {
        Map<Character, Integer> charMaps = new HashMap<>();
        for (char c : password.toCharArray()) {
            if (!charMaps.containsKey(c)) {
                charMaps.put(c, 1);
            } else {
                charMaps.put(c, charMaps.get(c) + 1);
            }
        }
        Integer[] vector = new Integer[wordBag.size()];
        for (int i = 0; i < wordBag.size(); i++) {
            Integer value = charMaps.get(wordBag.get(i));
            vector[i] = value == null ? 0 : value;
        }
        return vector;
    }

    public static void main(String[] args) {
        PassowdEvalaute passowdEvalaute = new PassowdEvalaute("密码评估");
        HashSet<String> historyPassword = new HashSet<>();
        historyPassword.add("souhu234");
        historyPassword.add("shangxing456");
        passowdEvalaute.eval("234souhu", historyPassword);
    }
}
