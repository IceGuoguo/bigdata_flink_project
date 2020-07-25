package com.bing.controller;

import com.bing.entity.User;
import com.bing.exceptions.UserNameAndPasswordException;
import com.bing.exceptions.VerifyCodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author guobing
 * @version 1.0
 * @date 2019/9/4 下午4:18
 * @description
 */
@RestController
@RequestMapping(value = "/bingApp")
public class BingWebAppController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BingWebAppController.class);

    @Autowired
    private RestTemplate restTemplate;

    //用户注册
    @PostMapping("/register")
    public void register(User user,
                           @RequestParam(value = "multipartFile",required = false) MultipartFile multipartFile,
                           @RequestParam(value = "verifyCode") String verifyCode,
                           HttpSession session) {

        LOGGER.debug(user.toString());
        LOGGER.debug("verifyCode:"+verifyCode);
        LOGGER.debug("multipartFile:"+multipartFile.getOriginalFilename());

        //获取会话verifyCode
        String sessionVerifyCode = (String) session.getAttribute("verifyCode");

        if(sessionVerifyCode!=null && verifyCode!=null && verifyCode.equalsIgnoreCase(sessionVerifyCode)){
            //删除此次的会话verifyCode
            session.removeAttribute("verifyCode");
            if(multipartFile!=null && !multipartFile.getOriginalFilename().isEmpty()){
                LOGGER.debug("存储文件:"+multipartFile.getOriginalFilename());
                user.setPhoto(multipartFile.getOriginalFilename());
            }
        }else{
            throw new VerifyCodeException("验证码错误！");
        }

        String url = "http://localhost:8888/UserModel/managerUser/registerUser";

        restTemplate.postForObject(url, user, User.class);
    }

    //用户登录
    @PostMapping(value = "/userLogin")
    public void login(User user,
                        @RequestParam(value = "verifyCode") String verifyCode,
                        @CookieValue(name = "inputVector",required = false) String inputVector,
                        @RequestHeader(name = "User-Agent") String agentInfo,
                        HttpSession session,
                        HttpServletRequest request) {

        LOGGER.debug("userLogin:"+user);
        LOGGER.debug("verifyCode:"+verifyCode);
        LOGGER.debug("inputVector:"+inputVector);
        LOGGER.debug("agentInfo:"+agentInfo);

        String sessionVerifyCode = (String) session.getAttribute("verifyCode");
        if(sessionVerifyCode!=null && verifyCode!=null && verifyCode.equalsIgnoreCase(sessionVerifyCode)) {
            String url = "http://localhost:8888/UserModel/managerUser/userLogin";

            User loginUser = restTemplate.postForObject(url, user, User.class);

            LOGGER.debug("user====>:"+loginUser);

            if(loginUser==null){
                throw new UserNameAndPasswordException("用户名字，密码不存在!");
            }
            session.setAttribute("user",loginUser);
        }else{
            throw new VerifyCodeException("验证码错误！");
        }

//        return "ok";

    }
    @GetMapping("test")
    public void test(){
        System.out.println("---------------");
    }

    //分页展示所有用户
    @GetMapping("/showAll")
    public String showAll(@RequestParam(value = "page",defaultValue = "1") String page,
                          @RequestParam(value = "rows",defaultValue = "5") String rows,
                          String column, Object value, Model model) {
        LOGGER.debug(page+","+rows+","+column+","+value.toString());
        String url = "http://localhost:8888/UserModel/managerUser/queryUserByPage?page={page}&rows={rows}&column={column}&value={value}";

        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        params.put("rows", rows);
        params.put("column", column);
        params.put("value", value);

        User[] users = restTemplate.getForObject(url, User[].class, params);

        model.addAttribute("users",users);

        return "queryAll_bak";
    }

    //批量删除
    @DeleteMapping("/deleteUserByIds")
    public String deleteUserByIds(Integer[] ids) {
        LOGGER.debug(ids.toString());
        String url="http://localhost:8888/UserModel/managerUser/deleteUserByIds?ids={ids}";

        Map<String ,Integer[]> params = new HashMap<>();
        params.put("ids",ids);
        restTemplate.delete(url,params);

        return "redirect:/bingApp/showAll";
    }

    //更新用户信息
    @PutMapping("/updateUserById")
    public String updateUserById(User user){
        LOGGER.debug(user.toString());
        String url="http://localhost:8888/UserModel/managerUser/updateUser";

        restTemplate.put(url,user);
        return "redirect:/bingApp/showAll";
    }

    @PostMapping("/addUser")
    public String addUser(User user){

        LOGGER.debug(user.toString());
        String url = "http://localhost:8888/UserModel/managerUser/addUser";

        restTemplate.postForObject(url, user, User.class);
        return "redirect:/bingApp/showAll";
    }

    @GetMapping("/queryUserById")
    public String queryUserById(Integer id){
        LOGGER.debug(id+"");
        String url = "http://localhost:8888/UserModel/managerUser/queryUserById";

        restTemplate.postForObject(url,id,User.class);
        return "updateUser";
    }


}
