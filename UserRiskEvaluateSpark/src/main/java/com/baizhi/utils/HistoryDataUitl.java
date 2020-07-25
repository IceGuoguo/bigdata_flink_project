package com.baizhi.utils;

import com.baizhi.model.HistoryData;
import com.baizhi.model.LoginSuccessData;

import java.util.*;

/**
 星期一 : "10:1,13:3,20:4,23:3,18:4'
 private var loginHabits: Map[String, String] = new HashMap[String, String]
 // 存储最近的10次输入特征
 private var historyInputFutures: List[Array[Integer]] = new ArrayList[Array[Integer]]
 //历史密码和当前密码 - 乱序
 private var historyPasswords: Set[String] = new HashSet[String]

 //记录用户历史登陆城市
 private var historyCity: Set[String] = new HashSet[String]
 private var latitude: Double = 0.0// 用户上一次 纬度

 private var longitude: Double = 0.0//用户上一次 经度

 private var lastLoginDate: Long = 0L
 //记录上一次用户登陆设备信息
 private var lastAgent: String = null
 */
public class HistoryDataUitl {
    public static void updateHistoryData(LoginSuccessData loginSuccessData, HistoryData historyData){
        //1.处理用户登陆习惯
        long loginTime = loginSuccessData.getTime();
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(new Date(loginTime));
        //当前星期数
        String[] weeks={"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};
        String currentWeeks=weeks[calendar.get(Calendar.DAY_OF_WEEK)-1];
        String curentHour= calendar.get(Calendar.HOUR_OF_DAY)+"";

        Map<String, String> loginHabits = historyData.getLoginHabits();
        if(loginHabits.containsKey(currentWeeks)){//如果当前周登陆过，更新对应的小时计数
            String loginHourCounts = loginHabits.get(currentWeeks);// 小时:次数,...
            Map<String,Integer> loginHourCountMap=new HashMap<>();
            for (String token : loginHourCounts.split(",")) {
                String hour = token.split(":")[0];
                Integer count =Integer.parseInt(token.split(":")[1]);
                loginHourCountMap.put(hour,count);
            }
            Integer count=0;
            if(loginHourCountMap.containsKey(curentHour)){//在当前小时 有过登陆记录
               count=loginHourCountMap.get(curentHour);
            }
            loginHourCountMap.put(curentHour,count+1);
            //将更新后的本周数据转换为  小时:次数,...
            String loginHourCountsStr = loginHourCountMap.entrySet().stream()
                                        .map(entry -> entry.getKey() + ":" + entry.getValue())
                                        .reduce((v1, v2) -> v1 + "," + v2)
                                        .get();
            loginHabits.put(currentWeeks,loginHourCountsStr);
        }else{
            loginHabits.put(currentWeeks,curentHour+":"+1);
        }

        //2.存储最近的10次输入特征
        List<Integer[]> historyInputFutures = historyData.getHistoryInputFutures();
        historyInputFutures.add(loginSuccessData.getInputFutures());
        int size=historyInputFutures.size();
        List<Integer[]> subList=new ArrayList<>();
        if(size>10){
            for(int i=size-10;i<size;i++){
                subList.add(historyInputFutures.get(i));
            }
            historyData.setHistoryInputFutures(subList);
        }
        //3.存储历史密码
        historyData.getHistoryPasswords().add(loginSuccessData.getPassword());
        //4.设置登陆地区和上一次登陆位置
        historyData.getHistoryCity().add(loginSuccessData.getCity());
        historyData.setLastLoginDate(loginSuccessData.getTime());
        historyData.setLatitude(loginSuccessData.getLatitude());
        historyData.setLongitude(loginSuccessData.getLongtitude());
        //5.设备信息
        historyData.setLastAgent(loginSuccessData.getAgentInfo());

    }

}
