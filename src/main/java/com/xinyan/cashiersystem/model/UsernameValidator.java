package com.xinyan.cashiersystem.model;


import com.xinyan.cashiersystem.controller.ErrorRedirectException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component  // 交给 Spring 管理
public class UsernameValidator {
    public String validate(String module, String redirectUrl, String username) {
        // 用户名的要求：不是 null && 不是 "" && 长度不能超过 50 && 不能重复
        if (username == null) {
            throw new ErrorRedirectException("username 是 null", module, redirectUrl);
        }

        username = username.trim(); // 去掉两边的空格
        if (username.isEmpty()) {
            throw new ErrorRedirectException("username 是 \"\"", module, redirectUrl);
        }

        if (username.length() > 50) {
            throw new ErrorRedirectException("username 的长度超过 50", module, redirectUrl);
        }

        return username;
    }
}
