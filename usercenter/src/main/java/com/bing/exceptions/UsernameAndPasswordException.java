package com.bing.exceptions;

/**
 * @author guobing
 * @version 1.0
 * @date 2019/9/4 下午3:53
 * @description
 */

public class UsernameAndPasswordException extends RuntimeException {
    public UsernameAndPasswordException(String message){
        super(message);
    }
}
