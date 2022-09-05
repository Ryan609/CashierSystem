package com.xinyan.cashiersystem.mapper;


import com.xinyan.cashiersystem.model.order.Order;
import com.xinyan.cashiersystem.model.order.OrderStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository
@Mapper
public interface OrderMapper {
    void insert(Order order);

    void updateConfirm(
            @Param("orderId") int orderId,
            @Param("finishedAt")Timestamp finishedAt,
            @Param("status")OrderStatus status);

    void deleteByOrderId(int orderId);
}