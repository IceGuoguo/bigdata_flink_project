package com.bing.entity;

import java.io.Serializable;

public class ErrorMessage implements Serializable {
    private Integer erroCode;
    private String message;

    public ErrorMessage(Integer erroCode, String message) {
        this.erroCode = erroCode;
        this.message = message;
    }

    public Integer getErroCode() {
        return erroCode;
    }

    public void setErroCode(Integer erroCode) {
        this.erroCode = erroCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
