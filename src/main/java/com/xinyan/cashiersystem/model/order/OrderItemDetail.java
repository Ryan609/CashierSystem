package com.xinyan.cashiersystem.model.order;

import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

/**
 * @author: xinyan
 * @data: 2022/09/06/10:16
 **/
@Setter
@ToString
@EqualsAndHashCode
public class OrderItemDetail {
    private int productId;
    private String productName;
    private int productPrice;
    private int productNumber;
    private String productUnit;
    private int productDiscount;


    public OrderItemDetail() {}

    public int getId() {
        return productId;
    }

    public String getName() {
        return productName;
    }

    public double getPrice() {
        return productPrice / 100.0;
    }

    public int getNumber() {
        return productNumber;
    }

    public String getUnit() {
        return productUnit;
    }

    public double getPayable() {
        return getPrice() * productNumber;
    }

    public double getActual() {
        return getPayable() * productDiscount / 100.0;
    }
}
