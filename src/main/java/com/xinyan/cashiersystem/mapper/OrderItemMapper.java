package com.xinyan.cashiersystem.mapper;


import com.xinyan.cashiersystem.model.order.OrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
@Mapper
public interface OrderItemMapper {

    void insertBatch(Set<OrderItem> orderItemsSet);

    List<OrderItem> selectByOrderId(int orderId);

    void deleteByOrderId(int orderId);
}
