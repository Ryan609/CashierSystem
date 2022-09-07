package com.xinyan.cashiersystem.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.xinyan.cashiersystem.model.User;
import com.xinyan.cashiersystem.model.order.Order;
import com.xinyan.cashiersystem.model.product.Product;
import com.xinyan.cashiersystem.service.OrderService;
import com.xinyan.cashiersystem.service.ProductService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: xinyan
 * @data: 2022/09/04/21:33
 **/
@Slf4j
@RestController
public class JsonController {
    private final ProductService productService;
    private final OrderService orderService;
    @Autowired
    public JsonController(ProductService productService, OrderService orderService) {
        this.productService = productService;
        this.orderService = orderService;
    }

    @Slf4j
    @Data
    private static class ProductView {
        private String name;
        private String introduce;
        private int stock;
        private String unit;
        private double price;
        private int discount;

        public ProductView() {}

        public ProductView(Product product) {
            log.debug("ProductView(product = {})", product);
            this.name = product.getName();
            this.introduce = product.getIntroduce();
            this.stock = product.getStock();
            this.unit = product.getUnit();
            this.price = product.getPrice() / 100.0;    // 必须是 100.0 而不能是 100
            this.discount = product.getDiscount();
        }
    }

    @Slf4j
    @Data
    private static class ProductListView {
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String redirectUrl;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private List<ProductView> data;

        public static ProductListView success(List<Product> productList) {
            ProductListView view = new ProductListView();
            // Product 的线性表 -> ProductView 的线性表
            // 如果下面的 Stream 写法，看不懂，就看这个就可以
//            view.data = new ArrayList<>();
//            for (Product product : productList) {
//                ProductView productView = new ProductView(product);
//                view.data.add(productView);
//            }
            // 链式写法
            view.data = productList.stream()
                    .map(ProductView::new)
                    .collect(Collectors.toList());

            return view;
        }

        public static ProductListView failure(String redirectUrl) {
            // 注意，这里不是 HTTP 协议层面的重定向，我们一会儿会让前端进行跳转
            ProductListView view = new ProductListView();
            view.redirectUrl = redirectUrl;
            return view;
        }
    }

    @GetMapping("/product/list.json")
    public ProductListView getProductList(HttpServletRequest request) {
        User currentUser = null;
        HttpSession session = request.getSession(false);
        if (session != null) {
            currentUser = (User) session.getAttribute("currentUser");
        }
        if (currentUser == null) {
            // 说明没有登录
            return ProductListView.failure("/login.html");
        }

        List<Product> productList = productService.getList();
        return ProductListView.success(productList);
    }



    @Data
    private static class OrderView {
        private String uuid;
        private String status;
        private Timestamp createdAt;
        private Timestamp finishedAt;

        public OrderView() {}

        OrderView(Order order) {
            this.uuid = order.getUuid();
            this.status = order.getStatus().toString();
            this.createdAt = order.getCreatedAt();
            this.finishedAt = order.getFinishedAt();
        }
    }

    @Data
    private static class OrderListView {
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String redirectUrl;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private List<OrderView> data;

        public static OrderListView success(List<Order> orderList) {
            OrderListView view = new OrderListView();
            view.data = orderList.stream()
                    .map(OrderView::new)
                    .collect(Collectors.toList());
            return view;
        }

        public static OrderListView failure(String redirectUrl) {
            OrderListView view = new OrderListView();
            view.redirectUrl = redirectUrl;
            return view;
        }
    }

    @GetMapping("/order/list.json")
    public OrderListView orderList(HttpServletRequest request) {
        User currentUser = null;
        HttpSession session = request.getSession(false);
        if (session != null) {
            currentUser = (User) session.getAttribute("currentUser");
        }

        if (currentUser == null) {
            // 说明没有登录
            return OrderListView.failure("/login.html");
        }

        List<Order> orderList = orderService.getList();

        return OrderListView.success(orderList);
    }
}
