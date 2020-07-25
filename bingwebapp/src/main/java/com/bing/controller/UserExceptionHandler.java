package com.bing.controller;

import com.bing.entity.ErrorMessage;
import com.bing.exceptions.UserNameAndPasswordException;
import com.bing.exceptions.VerifyCodeException;
import org.springframework.web.bind.annotation.*;

//@ControllerAdvice
@RestControllerAdvice
public class UserExceptionHandler  {
    //@ResponseBody
    @ExceptionHandler(value = VerifyCodeException.class)
    public ErrorMessage handlerError(VerifyCodeException ex){
        return new ErrorMessage(1002,ex.getMessage());
    }

    //@ResponseBody
    @ExceptionHandler(value = UserNameAndPasswordException.class)
    public ErrorMessage handlerError(UserNameAndPasswordException ex){
        System.out.println("-----------");
        return new ErrorMessage(1001,ex.getMessage());
    }

}
