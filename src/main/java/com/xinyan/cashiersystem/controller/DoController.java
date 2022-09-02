package com.xinyan.cashiersystem.controller;

import com.xinyan.cashiersystem.model.PasswordValidator;
import com.xinyan.cashiersystem.model.User;
import com.xinyan.cashiersystem.model.UsernameValidator;
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
    private final UsernameValidator usernameValidator;
    private final PasswordValidator passwordValidator;

    @Autowired
    public DoController(UserService userService, UsernameValidator usernameValidator, PasswordValidator passwordValidator) {
        this.userService = userService;
        this.usernameValidator = usernameValidator;
        this.passwordValidator = passwordValidator;
    }

    @PostMapping("/register.do")
    public String register(String username, String password) {
        log.debug("用户注册: username = {}, password = {}", username, password);

        username = usernameValidator.validate(username);
        password = passwordValidator.validate(password);

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
