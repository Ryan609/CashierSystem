package com.xinyan.cashiersystem.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.xinyan.cashiersystem.model.User;
import com.xinyan.cashiersystem.model.product.Product;
import com.xinyan.cashiersystem.service.ProductService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
    @Autowired
    public JsonController(ProductService productService) {
        this.productService = productService;
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

            /*
            // Stream 的写法
            Stream<Product> stream = productList.stream();
            // 针对其中的每一项（类型就是 Product)，调用一个方法
            // lambda 表达式？
            // (Product p) -> { return "hello" }    如果非要理解，其实就是只有一个方法的一个匿名对象
            // p -> { return "hello" }
            // p -> "hello"
            // ProductView::new  是方法引用语法 类名::方法名，就是 ProductView 这个类的构造方法
            // 针对 这个 stream 中的每一项（每一项都是一个 Product 对象）
            // 调用传入的这个方法 Product::new
            // 调用完成之后，每一项 Product 都得到了一个新的 ProductView 对象了
            // 再收集起来，作为 Stream<ProductView> stream2
            Stream<ProductView> stream2 = stream.map(ProductView::new);
            // 最后，再收集成 List<ProductView>
            view.data = stream2.collect(Collectors.toList());
             */

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
}
