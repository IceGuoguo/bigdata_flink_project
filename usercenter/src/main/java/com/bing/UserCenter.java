package com.bing;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author guobing
 * @version 1.0
 * @date 2019/9/3 下午3:55
 * @description
 */

@SpringBootApplication
@MapperScan(basePackages = "com.bing.dao")
public class UserCenter {
    public static void main(String[] args) {
        SpringApplication.run(UserCenter.class,args);
    }
}
