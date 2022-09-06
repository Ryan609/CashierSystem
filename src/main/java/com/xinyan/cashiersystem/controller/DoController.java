package com.xinyan.cashiersystem.controller;


import com.xinyan.cashiersystem.model.PasswordValidator;
import com.xinyan.cashiersystem.model.User;
import com.xinyan.cashiersystem.model.UsernameValidator;
import com.xinyan.cashiersystem.model.order.Order;
import com.xinyan.cashiersystem.model.order.OrderDetail;
import com.xinyan.cashiersystem.model.product.Product;
import com.xinyan.cashiersystem.model.product.ProductParam;
import com.xinyan.cashiersystem.service.OrderService;
import com.xinyan.cashiersystem.service.ProductService;
import com.xinyan.cashiersystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

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
    private final ProductService productService;
    private final OrderService orderService;

    @Autowired
    public DoController(UserService userService, UsernameValidator usernameValidator, PasswordValidator passwordValidator, ProductService productService, OrderService orderService) {
        this.userService = userService;
        this.usernameValidator = usernameValidator;
        this.passwordValidator = passwordValidator;
        this.productService = productService;
        this.orderService = orderService;
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
    public String productCreate(ProductParam productParam, HttpServletRequest request) {
        String module = "用户登录";
        String redirectUrl = "/login.html";
        log.debug("{}: 请求参数 = {}", module, productParam);

        User currentUser = null;
        HttpSession session = request.getSession(false);
        if (session != null) {
            currentUser = (User) session.getAttribute("currentUser");
        }

        if (currentUser == null) {
            // 说明用户未登录
            log.debug("{}: 用户未登录，无权进行该操作", module);
            return "redirect:/login.html";  // 重定向到登录页，让用户登录
        }
        Product product = productService.create(currentUser, productParam);
        return "redirect:/product/list.html";
    }

    @PostMapping("/product/update.do")
    public String productUpdate(ProductParam productParam, HttpServletRequest request) {
        log.debug("商品更新: 请求参数 = {}", productParam);

        User currentUser = null;
        HttpSession session = request.getSession(false);
        if (session != null) {
            currentUser = (User) session.getAttribute("currentUser");
        }
        if (currentUser == null) {
            // 说明用户未登录
            log.debug("商品更新: 用户未登录，无权进行该操作");
            return "redirect:/login.html";  // 重定向到登录页，让用户登录
        }

        Product product = productService.update(currentUser, productParam);

        log.debug("商品更新: 成功，product = {}", product);
        return "redirect:/product/list.html";
    }

    @PostMapping("/order/create.do")
    public String orderCreate(@RequestParam("create-param") String createParam, HttpServletRequest request) {
        log.debug("购买商品: createParam = {}", createParam);

        Map<Integer, Integer> toBoughtProductMap = new HashMap<>();
        // TODO: 暂时不考虑参数合法性的问题
        String[] split = createParam.split(",");
        for (String s : split) {
            String[] group = s.split("-");
            String productIdStr = group[0];     // 参数不对，就会抛异常
            String numberStr = group[1];     // 参数不对，就会抛异常

            int productId = Integer.parseInt(productIdStr);     // 参数不对，就会抛异常
            int number = Integer.parseInt(numberStr);           // 参数不对，就会抛异常

            toBoughtProductMap.put(productId, number);
        }

        log.debug("toBoughtProductMap = {}", toBoughtProductMap);

        User currentUser = null;
        HttpSession session = request.getSession(false);
        if (session != null) {
            currentUser = (User) session.getAttribute("currentUser");
        }

        if (currentUser == null) {
            // 说明用户未登录
            log.debug("商品更新: 用户未登录，无权进行该操作");
            return "redirect:/login.html";  // 重定向到登录页，让用户登录
        }

        log.debug("当前用户: user = {}", currentUser);
        Order order = orderService.create(currentUser, toBoughtProductMap);
        log.debug("创建订单: {}", order);

        return "redirect:/order/detail/" + order.getUuid();
    }

    @GetMapping("/order/detail/{uuid}")
    public String orderDetail(@PathVariable("uuid") String uuid, HttpServletRequest request, Model model) {
        log.debug("订单详情: uuid = {}", uuid);

        User currentUser = null;
        HttpSession session = request.getSession(false);
        if (session != null) {
            currentUser = (User) session.getAttribute("currentUser");
        }

        if (currentUser == null) {
            // 说明用户未登录
            log.debug("商品更新: 用户未登录，无权进行该操作");
            return "redirect:/login.html";  // 重定向到登录页，让用户登录
        }
        log.debug("当前用户: user = {}", currentUser);

        OrderDetail orderDetail = orderService.query(uuid);

        log.debug("order = {}", orderDetail);

        model.addAttribute("order", orderDetail);

        return "order-detail";
    }
}
