package com.xinyan.cashiersystem.controller;

import com.xinyan.cashiersystem.model.User;
import com.xinyan.cashiersystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author: xinyan
 * @data: 2022/09/01/22:03
 **/
@Slf4j
@Controller
public class DoController {
    private final UserService userService;

    @Autowired
    public DoController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register.do")
    public String register(String username, String password) {
        log.debug("用户注册: username = {}, password = {}", username, password);

        // 进行参数的合法性校验
        // 用户名的要求：不是 null && 不是 "" && 长度不能超过 50 && 不能重复
        if (username == null) {
            throw new ErrorRedirectException("username 是 null");
        }

        username = username.trim(); // 去掉两边的空格
        if (username.isEmpty()) {
            throw new ErrorRedirectException("username 是 \"\"");
        }

        if (username.length() > 50) {
            throw new ErrorRedirectException("username 的长度超过 50");
        }

        // username 是一个合法的用户名

        // 密码的要求：不是 null && 不是 ""
        // TODO: 长度必须超过 8 个字符 && 必须有数字 + 字母
        if (password == null) {
            throw new ErrorRedirectException("password 是 null");
        }

        password = password.trim();
        if (password.isEmpty()) {
            throw new ErrorRedirectException("password 是 \"\"");
        }

        // password 是一个合法的密码

        // 完成注册的工作
        try {
            User user = userService.register(username, password);

            // 最终注册成功之后，跳转到首页(/)
            log.debug("用户注册: 注册成功, user = {}", user);
            return "redirect:/";
        } catch (DuplicateKeyException exc) {
            throw new ErrorRedirectException("username 重复", exc);
        }
    }
}
