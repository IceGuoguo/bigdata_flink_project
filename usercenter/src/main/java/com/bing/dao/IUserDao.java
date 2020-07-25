package com.bing.dao;

import com.bing.entity.User;
import com.bing.entity.UserVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author guobing
 * @version 1.0
 * @date 2019/9/3 下午3:08
 * @description
 */

@Repository
public interface IUserDao {
    public void saveUser(User user);
    public User queryUserByNameAndPassword(User user);
    public List<User> queryUserByPage(@Param("beginIndex") Integer beginIndex, @Param("pageSize") Integer pageSize, @Param("column") String column, @Param("value") Object value);
    //public List<User> queryUserByPage(@Param("beginIndex") Integer beginIndex, @Param("pageSize") Integer pageSize);
    public int queryUserCount(@Param("column") String column, @Param("value") Object value);
    public User queryUserById(Integer id);
    public void deleteUserByIds(@Param("id") Integer id);
    public void updateUser(User user);
}
