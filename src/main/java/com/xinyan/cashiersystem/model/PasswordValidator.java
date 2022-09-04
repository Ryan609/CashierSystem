package com.xinyan.cashiersystem.model;

import com.xinyan.cashiersystem.controller.ErrorRedirectException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PasswordValidator {
    public String validate(String module, String redirectUrl, String password) {
        // 密码的要求：不是 null && 不是 ""
        // TODO: 长度必须超过 8 个字符 && 必须有数字 + 字母
        if (password == null) {
            throw new ErrorRedirectException("password 是 null", module, redirectUrl);
        }

        password = password.trim();
        if (password.isEmpty()) {
            throw new ErrorRedirectException("password 是 \"\"", module, redirectUrl);
        }

        return password;
    }
}
