package com.xinyan.cashiersystem.controller;

import com.xinyan.cashiersystem.model.PasswordValidator;
import com.xinyan.cashiersystem.model.User;
import com.xinyan.cashiersystem.model.UsernameValidator;
import com.xinyan.cashiersystem.model.product.ProductParam;
import com.xinyan.cashiersystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
    public String register(String username, String password, HttpServletRequest request) {
        String module = "用户注册";
        String redirectUrl = "/register.html";
        log.debug("{}: username = {}, password = {}", module, username, password);

        username = usernameValidator.validate(module, redirectUrl,username);
        password = passwordValidator.validate(module,redirectUrl,password);

        // 完成注册的工作
        try {
            User user = userService.register(username, password);

            // 直接完成登录, 把信息放到session中.
            HttpSession httpSession = request.getSession();
            httpSession.setAttribute("currentUser", user);

            // 最终注册成功之后，跳转到首页(/)
            log.debug("{}: 注册成功, user = {}", module, user);
            return "redirect:/";
        } catch (DuplicateKeyException exc) {
            throw new ErrorRedirectException("username 重复", module, redirectUrl, exc);
        }
    }

    @PostMapping("/login.do")
    public String login(String username, String password, HttpServletRequest request) {
        String module = "用户登录";
        String redirectUrl = "/login.html";
        log.debug("{}: username = {}, password = {}", module, username, password);

        username = usernameValidator.validate(module, redirectUrl, username);
        password = passwordValidator.validate(module, redirectUrl, password);

        User user = userService.login(username, password);

        if (user == null) {
            throw new ErrorRedirectException("用户名或密码错误", module, redirectUrl);
        }

        HttpSession httpSession = request.getSession();
        httpSession.setAttribute("currentUser", user);

        log.debug("{}: 登录成功, user = {}", module, user);
        return "redirect:/";
    }

    @PostMapping("/product/create.do")
    public String productCreate(ProductParam productParam) {
        String module = "用户登录";
        String redirectUrl = "/login.html";
        log.debug("{}: 请求参数 = {}", module, productParam);


        return "redirect:/product/list.html";
    }


}
