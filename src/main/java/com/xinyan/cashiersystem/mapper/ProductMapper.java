package com.xinyan.cashiersystem.mapper;


import com.xinyan.cashiersystem.model.product.Product;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ProductMapper {
    void insert(Product product);

    List<Product> selectAll();
}
