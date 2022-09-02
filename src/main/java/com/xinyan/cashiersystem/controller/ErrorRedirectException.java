package com.xinyan.cashiersystem.controller;

/* 错误然后重定向的异常，希望是一个非受查异常（继承在 RuntimeException）*/
public class ErrorRedirectException extends RuntimeException {
    private final String error;

    public ErrorRedirectException(String error) {
        // 调用父类的构造方法
        super();    // 这句可以省略

        this.error = error;
    }

    public ErrorRedirectException(String error, Throwable cause) {
        // 调用父类的构造方法
        super(cause);   // 这句就不能省略了

        this.error = error;
    }

    public String getError() {
        return error;
    }


}
