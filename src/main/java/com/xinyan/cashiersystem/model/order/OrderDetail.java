package com.xinyan.cashiersystem.model.order;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author: xinyan
 * @data: 2022/09/06/10:15
 **/
@Data
public class OrderDetail {
    private int orderId;
    private int userId;
    private String username;
    private String uuid;
    private Timestamp createdAt;
    private OrderStatus status;
    private int payable;
    private int actual;
    private List<OrderItemDetail> itemList;

    public void setStatus(int status) {
        if (status == 1) {
            this.status = OrderStatus.Unpaid;
        } else {
            this.status = OrderStatus.Paid;
        }
    }

    public String getStatus() {
        return status.toString();
    }

    public double getPayable() {
        return payable / 100.0;
    }

    public double getActual() {
        return actual / 100.0;
    }
}
