package com.xinyan.cashiersystem.model.product;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: xinyan
 * @data: 2022/09/04/15:39
 **/
@Slf4j
@Data
public class ProductParam {
    private String productId;
    private String name;
    private String introduce;
    private String stock;
    private String unit;
    private String price;
    private String discount;

    public ProductParam() {
        log.debug("model.product.ProductParam()");
    }

    public void setName(String name) {
        log.debug("model.product.ProductParam.setName(name = {})", name);
        this.name = name;
    }

    public void setIntroduce(String introduce) {
        log.debug("model.product.ProductParam.setIntroduce(introduce = {})", introduce);
        this.introduce = introduce;
    }

    public void setStock(String stock) {
        log.debug("model.product.ProductParam.setStock(stock = {})", stock);
        this.stock = stock;
    }

    public void setUnit(String unit) {
        log.debug("model.product.ProductParam.setUnit(unit = {})", unit);
        this.unit = unit;
    }

    public void setPrice(String price) {
        log.debug("model.product.ProductParam.setPrice(price = {})", price);
        this.price = price;
    }

    public void setDiscount(String discount) {
        log.debug("model.product.ProductParam.setDiscount(discount = {})", discount);
        this.discount = discount;
    }
}
