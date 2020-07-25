package com.bing.service.impl;

import com.bing.dao.IUserDao;
import com.bing.entity.User;
import com.bing.exceptions.UsernameAndPasswordException;
import com.bing.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author guobing
 * @version 1.0
 * @date 2019/9/3 下午3:35
 * @description
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS)
public class UserService implements IUserService {

    @Autowired
    private IUserDao userDao;

    @Transactional(propagation = Propagation.REQUIRED,readOnly = false)
    @Override
    public void saveUser(User user) {

        userDao.saveUser(user);

    }

    @Override
    public User queryUserByNameAndPassword(User user) {
        User u = userDao.queryUserByNameAndPassword(user);
        /*if (u == null){
            throw new UsernameAndPasswordException("用户名或者密码不正确！");
        }*/
        return u;
    }

    @Override
    public List<User> queryUserByPage(Integer pageNow, Integer pageSize, String column, Object value) {

        int beginIndex = (pageNow - 1) * pageSize;

        return userDao.queryUserByPage(beginIndex, pageSize,column,value);
    }

    @Override
    public int queryUserCount(String column, Object value) {
        return userDao.queryUserCount(column,value);
    }

    @Override
    public User queryUserById(Integer id) {
        return userDao.queryUserById(id);
    }

    @Transactional(propagation = Propagation.REQUIRED,readOnly = false)
    @Override
    public void deleteByUserIds(Integer[] ids) {
        for (int i = 0; i < ids.length; i++) {
            userDao.deleteUserByIds(ids[i]);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED,readOnly = false)
    @Override
    public void updateUser(User user) {

        userDao.updateUser(user);
    }
}
