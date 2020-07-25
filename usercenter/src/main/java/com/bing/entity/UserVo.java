package com.bing.entity;

import java.io.Serializable;

/**
 * @author guobing
 * @version 1.0
 * @date 2019/9/4 上午1:21
 * @description
 */

public class UserVo implements Serializable {
    private String name;
    private String password;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
