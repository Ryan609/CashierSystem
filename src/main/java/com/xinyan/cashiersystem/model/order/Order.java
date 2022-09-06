package com.xinyan.cashiersystem.model.order;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @author: xinyan
 * @data: 2022/09/05/10:46
 **/
@Data
public class Order {
    private Integer orderId;
    private int userId;
    private String uuid;
    private Timestamp createdAt;
    private Timestamp finishedAt;
    private int payable;        // 应付金额
    private int actual;         // 实付金额
    private OrderStatus status; // 订单状态

    public Order() {}

    public Order(int userId, String uuid, Timestamp createdAt, int payable, int actual, OrderStatus status) {
        this.userId = userId;
        this.uuid = uuid;
        this.createdAt = createdAt;
        this.payable = payable;
        this.actual = actual;
        this.status = status;
    }

    public void setStatus(int status) {
        if (status == 1) {
            this.status = OrderStatus.Unpaid;
        } else {
            this.status = OrderStatus.Paid;
        }
    }
}
