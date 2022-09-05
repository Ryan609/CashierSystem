package com.xinyan.cashiersystem.model.order;

public enum OrderStatus {
    Unpaid(1), Paid(2);

    private final int value;

    // 未支付(1)
    OrderStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
