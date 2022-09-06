package com.xinyan.cashiersystem.service;

import com.xinyan.cashiersystem.mapper.OrderItemMapper;
import com.xinyan.cashiersystem.mapper.OrderMapper;
import com.xinyan.cashiersystem.mapper.ProductMapper;
import com.xinyan.cashiersystem.model.User;
import com.xinyan.cashiersystem.model.order.Order;
import com.xinyan.cashiersystem.model.order.OrderDetail;
import com.xinyan.cashiersystem.model.order.OrderItem;
import com.xinyan.cashiersystem.model.order.OrderStatus;
import com.xinyan.cashiersystem.model.product.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

/**
 * @author: xinyan
 * @data: 2022/09/05/10:47
 **/
@Slf4j
@Service
public class OrderService {
    private final ProductMapper productMapper;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    @Autowired
    public OrderService(ProductMapper productMapper, OrderMapper orderMapper, OrderItemMapper orderItemMapperl) {
        this.productMapper = productMapper;
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapperl;
    }

    @Transactional
    // toBoughtProductMap<商品 id, 要购买的数量>
    public Order create(User user, Map<Integer, Integer> toBoughtProductMap) {
        // 1. 确认商品的库存足够
        Map<Integer, Product> productMap = ConfirmStock(toBoughtProductMap);

        // 2. 商品减库存
        DecreaseStock(toBoughtProductMap);

        // 3. 创建订单记录
        Order order = CreatOrder(user, productMap, toBoughtProductMap);
        // 4. 创建订单项记录
        insertOrderItem(order, productMap, toBoughtProductMap);

        return order;
    }

    private Order CreatOrder(User user, Map<Integer, Product> productMap, Map<Integer, Integer> toBoughtProductMap) {
        String uuid = generateUUID();
        Timestamp createdAt = Timestamp.from(Instant.now());

        int payable = TotalPayable(productMap, toBoughtProductMap);

        int actual = ActualPayable(productMap, toBoughtProductMap);

        OrderStatus status = OrderStatus.Unpaid;
        Order order = new Order(user.getUserId(), uuid, createdAt, payable, actual, status);

        orderMapper.insert(order);
        return order;
    }

    private void insertOrderItem(Order order, Map<Integer, Product> productMap, Map<Integer, Integer> toBoughtProductMap) {
        Set<Integer> productIdSet = productMap.keySet();
        Set<OrderItem> orderItemsSet = new HashSet<>();
        for (Integer productId : productIdSet) {
            Product product = productMap.get(productId);
            int number = toBoughtProductMap.get(productId);

            OrderItem orderItem = new OrderItem(order, product, number);
            orderItemsSet.add(orderItem);
        }
        orderItemMapper.insertBatch(orderItemsSet);
    }

    private int ActualPayable(Map<Integer, Product> productMap, Map<Integer, Integer> toBoughtProductMap) {
        Set<Integer> productIdSet = productMap.keySet();

        int sum = 0;
        for (Integer productId : productIdSet) {
            int number = toBoughtProductMap.get(productId);
            Product product = productMap.get(productId);
            int price = product.getPrice();
            double discout = product.getDiscount() / 100.0;

            int productPayable = (int)(price * number * discout);
            sum += productPayable;
        }
        return sum;
    }

    private int TotalPayable(Map<Integer, Product> productMap, Map<Integer, Integer> toBoughtProductMap) {
        Set<Integer> productIdSet = productMap.keySet();

        int sum = 0;
        for (Integer productId : productIdSet) {
            int number = toBoughtProductMap.get(productId);
            Product product = productMap.get(productId);
            int price = product.getPrice();

            int productPayable = price * number;
            sum += productPayable;
        }
        return sum;
    }

    private String generateUUID() {
        String s = UUID.randomUUID().toString();
        return s.replace("-", "");
    }

    private void DecreaseStock(Map<Integer, Integer> toBoughtProductMap) {
        for (Map.Entry<Integer, Integer> entry : toBoughtProductMap.entrySet()) {
            int productId = entry.getKey();
            int number = entry.getValue();

            productMapper.decreaseStockByProductId(productId, number);
        }
    }

    private Map<Integer, Product> ConfirmStock(Map<Integer, Integer> toBoughtProductMap) {
        // 把所有的商品 id 收集起来
        Set<Integer> productIdSet = toBoughtProductMap.keySet();
        List<Product> productList = productMapper.selectProductListByProductIdSet(productIdSet);

        Map<Integer, Product> productMap = new HashMap<>();
        for (Product product : productList) {
            productMap.put(product.getProductId(), product);
        }
        // 比较库存是否够
        for (Integer productId : productIdSet) {
            int number = toBoughtProductMap.get(productId);
            Product product = productMap.get(productId);

            if (number > product.getStock()) {
                throw new RuntimeException(product.getProductId().toString());   // 说明我们这个商品的库存是不够的
            }
        }
        return productMap;
    }

    @Transactional
    public void confirm(int orderId) {
        Timestamp finishedAt = Timestamp.from(Instant.now());
        OrderStatus status = OrderStatus.Paid;

        orderMapper.updateConfirm(orderId, finishedAt, status);
    }

    @Transactional
    public void cancel(int orderId) {
        // 1. 根据 orderId 查询出这个订单涉及的所有商品 id 和 它对应的购买数量
        List<OrderItem> orderItemList = orderItemMapper.selectByOrderId(orderId);
        Map<Integer, Integer> toBoughtProductMap = new HashMap<>();
        for (OrderItem orderItem : orderItemList) {
            int productId = orderItem.getProductId();
            int number = orderItem.getProductNumber();
            toBoughtProductMap.put(productId, number);
        }

        // 2. 删除所有该 orderId 关联的 order_items 记录（删除订单项）
        orderItemMapper.deleteByOrderId(orderId);

        // 3. 删除该 orderId 对应的订单记录
        orderMapper.deleteByOrderId(orderId);

        // 4. 遍历每个商品，为每个商品增加库存
        for (Map.Entry<Integer, Integer> entry : toBoughtProductMap.entrySet()) {
            int productId = entry.getKey();
            int number = entry.getValue();

            productMapper.incrementStockByProductId(productId, number);
        }
    }

    public OrderDetail query(String uuid) {
        OrderDetail order = orderMapper.selectByUUID(uuid);

        order.setItemList(orderItemMapper.selectAllByOrderId(order.getOrderId()));

        return order;
    }
}
