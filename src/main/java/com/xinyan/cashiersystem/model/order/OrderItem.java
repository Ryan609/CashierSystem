package com.xinyan.cashiersystem.model.order;

import com.xinyan.cashiersystem.model.product.Product;
import lombok.Data;

/**
 * @author: xinyan
 * @data: 2022/09/05/10:46
 **/
@Data
public class OrderItem {
    private Integer id;
    private int orderId;
    private int productId;
    private String productName;
    private String productIntroduce;
    private int productNumber;
    private String productUnit;
    private int productPrice;
    private int productDiscount;

    public OrderItem() {}

    public OrderItem(Order order, Product product, int number) {
        this.orderId = order.getOrderId();
        this.productId = product.getProductId();
        this.productName = product.getName();
        this.productIntroduce = product.getIntroduce();
        this.productNumber = number;
        this.productUnit = product.getUnit();
        this.productPrice = product.getPrice();
        this.productDiscount = product.getDiscount();
    }
}
