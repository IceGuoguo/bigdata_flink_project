package com.bing.evaluate;

import com.bing.model.EvalauteData;
import com.bing.model.EvaluateReport;
import com.bing.model.HistoryData;

import java.util.*;
import java.util.stream.Collectors;

public class HabitEvalaute extends Evaluate {

    public HabitEvalaute(String type) {
        super(type);
    }

    @Override
    public void evalue(EvalauteData evalauteData, HistoryData historyData, EvaluateReport evaluateReport,
                       EvaluateChain chain) {
        evaluateReport.addReport(this.type,eval(evalauteData.getTime(),historyData.getLoginHabits()));
        chain.evaluate(evalauteData,historyData,evaluateReport);
    }
    //时间      星期一 : "10:1,13:3,20:4,23:3,18:4'
    public boolean eval(long time, Map<String,String> loginHabits){
        String[] weeks={"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(new Date(time));
        //用户当前登陆时间
        String  currentHour=calendar.get(Calendar.HOUR_OF_DAY)+"";

        String week=weeks[calendar.get(Calendar.DAY_OF_WEEK)-1];

        if(loginHabits==null && loginHabits.get(week)==null){//如果历史没有数据，则评估没有哦意义
            return false;//没有风险
        }
        //获取本周 历史登陆习惯 10:1,13:3,20:4,23:3,18:4
        String habits= loginHabits.get(week);
        if(habits==null){
            return true;
        }
        //安装次数排序 降序
        List<String> list = Arrays.asList(habits.split(",")).stream().sorted(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.split(":")[1].compareTo(o2.split(":")[1])*-1;
            }
        }).collect(Collectors.toList());

        //获取到子集合 获取集合1/3位置作为习惯，如果元素少于3条，整体都是习惯
        Integer endIndex=list.size() / 3 == 0 ? list.size() : list.size() / 3;
        List<String> subList = list.subList(0,endIndex);
        List<String> newList=new ArrayList<String>();
        subList.forEach(item->newList.add(item));//元素拷贝
        //计算后续的列表中是否有次数相等的时间
        String lastElement = newList.get(endIndex-1);
        for(int i=endIndex;i<list.size(); i++){
            String ele = list.get(i);
            if(ele.split(":")[1].equals(lastElement.split(":")[1])){
                newList.add(ele);
            }else{
                break;
            }
        }
        //所有用户的 本周内 习惯小时
        List<String> habbitHours = newList.stream().map(habit -> habit.split(":")[0]).collect(Collectors.toList());

        return !habbitHours.contains(currentHour);
    }
}
