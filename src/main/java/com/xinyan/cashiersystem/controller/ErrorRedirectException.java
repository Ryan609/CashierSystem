package com.xinyan.cashiersystem.controller;

import lombok.Getter;

@Getter
/* 错误然后重定向的异常，希望是一个非受查异常（继承在 RuntimeException）*/
public class ErrorRedirectException extends RuntimeException {
    private final String error;
    private final String module;        // 哪个功能抛出的异常
    private final String redirectUrl;   // 重定向到哪

    public ErrorRedirectException(String error, String module, String redirectUrl) {
        // 调用父类的构造方法
        super();    // 这句可以省略

        this.error = error;
        this.module = module;
        this.redirectUrl = redirectUrl;
    }

    public ErrorRedirectException(String error, String module, String redirectUrl, Throwable cause) {
        // 调用父类的构造方法
        super(cause);   // 这句就不能省略了

        this.error = error;
        this.module = module;
        this.redirectUrl = redirectUrl;
    }


}
