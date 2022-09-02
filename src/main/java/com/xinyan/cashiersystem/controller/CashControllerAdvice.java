package com.xinyan.cashiersystem.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
// 添加这个注解之后，这个类会被作为异常统一处理的类 // AOP 思想的体现（AfterResourceHandler）
// AOP 里 Advice 是通知的意思
@ControllerAdvice
public class CashControllerAdvice {
    @ExceptionHandler(ErrorRedirectException.class)
    public String logAndRedirect(ErrorRedirectException exc) {
        // 暂时所有的错误都先重定向到注册页(/register.html)
        log.debug("用户注册: {}，注册失败", exc.getError());
        return "redirect:/register.html";
    }
}

