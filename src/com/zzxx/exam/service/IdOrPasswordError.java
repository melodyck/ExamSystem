package com.zzxx.exam.service;
//自定义异常类-编号或密码错误
public class IdOrPasswordError extends RuntimeException{
    public IdOrPasswordError() {
        super();
    }

    public IdOrPasswordError(String message) {
        super(message);
    }

    public IdOrPasswordError(String message, Throwable cause) {
        super(message, cause);
    }

    public IdOrPasswordError(Throwable cause) {
        super(cause);
    }

    protected IdOrPasswordError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
