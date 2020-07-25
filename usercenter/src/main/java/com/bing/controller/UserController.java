package com.bing.controller;

import com.bing.entity.User;
import com.bing.service.IUserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author guobing
 * @version 1.0
 * @date 2019/9/3 下午3:39
 * @description
 */

@RestController
@RequestMapping(value = "/managerUser")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private IUserService userService;

    @PostMapping(value = "/registerUser")
    public void registerUser(@RequestBody User user) {
        LOGGER.debug(user.toString());
        userService.saveUser(user);
    }

    @PostMapping(value = "/addUser")
    public void addUser(@RequestBody User user) {
        LOGGER.debug(user.toString());
        userService.saveUser(user);
    }

    @GetMapping(value = "/queryUserById")
    public User queryUserById(@RequestParam(value = "id") Integer id) {
        LOGGER.debug(id + "");
        //从数据库中查询
        return userService.queryUserById(id);
    }

    @GetMapping(value = "/queryUserCount")
    public Integer queryUserCount(@RequestParam(value = "column") String column,
                                  @RequestParam(value = "value") String value) {
        LOGGER.debug(column + "," + value);
        //从数据库中查询
        return userService.queryUserCount(column, value);
    }

    @GetMapping(value = "/queryUserByPage")
    public List<User> queryUserByPage(@RequestParam(value = "page", defaultValue = "1") Integer pageNow,
                                      @RequestParam(value = "rows", defaultValue = "10") Integer pageSize,
                                      @RequestParam(value = "column", required = false) String column,
                                      @RequestParam(value = "value", required = false) String value) {

        LOGGER.debug(pageNow + "," + pageSize + "," + column + "," + value);
        return userService.queryUserByPage(pageNow, pageSize, column, value);
    }


    @DeleteMapping(value = "/deleteUserByIds")
    public void deleteUserByIds(@RequestParam(value = "ids") Integer[] ids) {

        LOGGER.debug(ids.toString());
        userService.deleteByUserIds(ids);
    }

    @PutMapping(value = "/updateUser")
    public void updateUser(@RequestBody User user) {
        LOGGER.debug(user.toString());
        userService.updateUser(user);
    }

    @PostMapping(value = "/userLogin")
    public User userLogin(@RequestBody User user) {
        LOGGER.debug(user.toString());
        return userService.queryUserByNameAndPassword(user);
    }

}
