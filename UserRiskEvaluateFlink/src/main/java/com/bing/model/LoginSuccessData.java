package com.bing.model;

import java.io.Serializable;
import java.util.Arrays;

//INFO com.baizhi.controller.UserController#userLogin 2019-09-10 11:42:40 evaluate aap1 zhangsan 123456 西安市 108.92859,34.2583 758,2328,1743 [Mobile Safari Browser (mobile) 11.0 APPLE iOS 11 (iPhone)]
public class LoginSuccessData implements Serializable {
    private long time;//登陆的时间
    private String appName;//应用名
    private String username;
    private String password;//打乱顺序
    private String city;//登陆地区
    private Double latitude;//纬度
    private Double longtitude;//经度
    private Integer[] inputFutures; //输入特征
    private String agentInfo;//设备信息

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(Double longtitude) {
        this.longtitude = longtitude;
    }

    public Integer[] getInputFutures() {
        return inputFutures;
    }

    public void setInputFutures(Integer[] inputFutures) {
        this.inputFutures = inputFutures;
    }

    public String getAgentInfo() {
        return agentInfo;
    }

    public void setAgentInfo(String agentInfo) {
        this.agentInfo = agentInfo;
    }

    public LoginSuccessData(long time, String appName, String username, String password, String city, Double latitude, Double longtitude, Integer[] inputFutures, String agentInfo) {
        this.time = time;
        this.appName = appName;
        this.username = username;
        this.password = password;
        this.city = city;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.inputFutures = inputFutures;
        this.agentInfo = agentInfo;
    }

    public LoginSuccessData() {
    }

    @Override
    public String toString() {
        return "LoginSuccessData{" +
                "time=" + time +
                ", appName='" + appName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", city='" + city + '\'' +
                ", latitude=" + latitude +
                ", longtitude=" + longtitude +
                ", inputFutures=" + Arrays.toString(inputFutures) +
                ", agentInfo='" + agentInfo + '\'' +
                '}';
    }
}
