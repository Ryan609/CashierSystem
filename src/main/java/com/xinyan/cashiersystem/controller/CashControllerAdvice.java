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
        // 根据不同逻辑, 进行不同处理
        log.debug("{}: {}", exc.getModule(), exc.getError());
        return "redirect:" + exc.getRedirectUrl();
    }
}

