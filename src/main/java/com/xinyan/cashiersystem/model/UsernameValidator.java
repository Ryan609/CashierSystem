package com.xinyan.cashiersystem.model;


import com.xinyan.cashiersystem.controller.ErrorRedirectException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component  // 交给 Spring 管理
public class UsernameValidator extends AbsValidator{
    public String validate(String module, String redirectUrl, String username) {
        username = super.validate(module, redirectUrl, username);

        if (username.length() > 50) {
            throw new ErrorRedirectException("username 的长度超过 50", module, redirectUrl);
        }

        return username;
    }
}
