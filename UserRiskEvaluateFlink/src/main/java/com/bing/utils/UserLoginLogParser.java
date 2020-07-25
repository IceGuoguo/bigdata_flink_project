package com.bing.utils;

import com.bing.model.EvalauteData;
import com.bing.model.LoginSuccessData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//INFO com.bing.controller.UserController#userLogin 2019-09-10 11:42:40 evaluate aap1 zhangsan 123456 西安市 108.92859,34.2583 758,2328,1743 [Mobile Safari Browser (mobile) 11.0 APPLE iOS 11 (iPhone)]
public class UserLoginLogParser {
    private static final String REGEX_EXPRESS="^INFO\\s.*\\s([0-9]{4}-[0-9]{2}-[0-9]{2}\\s[0-9]{2}:[0-9]{2}:[0-9]{2})\\s(evaluate|success)\\s(.*)\\s(.*)\\s(.*)\\s(.*)\\s(.*)\\s(.*)\\s\\[(.*)\\]";

    /**
     * 解析日志
     * @param log
     * @return
     * @throws ParseException
     */
    public static EvalauteData parseEvalauteData(String log) throws ParseException {
        Pattern pattern = Pattern.compile(REGEX_EXPRESS);
        Matcher matcher = pattern.matcher(log);
        if(matcher.matches()){
            //解析时间
            SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date time = dateFormat.parse(matcher.group(1));

            //获取APP信息
            String appName=matcher.group(3);

            //获取用户名 密码 城市
            String username=matcher.group(4);
            String password=matcher.group(5);
            String city=matcher.group(6);

            //获取经纬度
            String[] point=matcher.group(7).split(",");
            Double longtitude=Double.parseDouble(point[0]);
            Double latitude=Double.parseDouble(point[1]);

            //解析出输入向量
            Object[] objects = Arrays.asList(matcher.group(8)
                    .split(","))
                    .stream()
                    .map(item -> Integer.parseInt(item)).toArray();
            Integer[] inputFutures = new Integer[objects.length];
            for (int i = 0; i < objects.length; i++) {
                inputFutures[i]=Integer.parseInt(objects[i].toString());
            }

            //解析设备信息
            String agent=matcher.group(9);
            return new EvalauteData(time.getTime(),appName,username,password,city,latitude,longtitude,inputFutures,agent);
        }else{
            return null;
        }
    }
    /**
     * 解析日志
     * @param log
     * @return
     * @throws ParseException
     */
    public static LoginSuccessData parseLoginSuccessData(String log) throws ParseException {
        Pattern pattern = Pattern.compile(REGEX_EXPRESS);
        Matcher matcher = pattern.matcher(log);
        if(matcher.matches()){
            //解析时间
            SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date time = dateFormat.parse(matcher.group(1));

            //获取APP信息
            String appName=matcher.group(3);

            //获取用户名 密码 城市
            String username=matcher.group(4);
            String password=matcher.group(5);
            String city=matcher.group(6);

            //获取经纬度
            String[] point=matcher.group(7).split(",");
            Double longtitude=Double.parseDouble(point[0]);
            Double latitude=Double.parseDouble(point[1]);

            //解析出输入向量
            Object[] objects = Arrays.asList(matcher.group(8)
                    .split(","))
                    .stream()
                    .map(item -> Integer.parseInt(item)).toArray();
            Integer[] inputFutures = new Integer[objects.length];
            for (int i = 0; i < objects.length; i++) {
                inputFutures[i]=Integer.parseInt(objects[i].toString());
            }

            //解析设备信息
            String agent=matcher.group(9);
            return new LoginSuccessData(time.getTime(),appName,username,password,city,latitude,longtitude,inputFutures,agent);
        }else{
            return null;
        }
    }

    /**
     * 是否格式符合
     * @param input
     * @return
     */
    public static Boolean isLegal(String input) {
        Pattern pattern = Pattern.compile(REGEX_EXPRESS);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }
}
