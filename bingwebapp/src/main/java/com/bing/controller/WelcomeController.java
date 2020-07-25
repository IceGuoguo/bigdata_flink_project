package com.bing.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

/**
 * @author guobing
 * @version 1.0
 * @date 2019/9/5 下午4:54
 * @description
 */

@Controller
public class WelcomeController {

    @RequestMapping("/")
    public String welcome(){
        return "WEB-INF/manager";
    }

    @RequestMapping("/login")
    public String login(){
        return "login_bak";
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "login_bak";
    }
}
