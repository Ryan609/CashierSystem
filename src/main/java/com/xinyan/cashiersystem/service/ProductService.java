package com.xinyan.cashiersystem.service;

import com.xinyan.cashiersystem.mapper.ProductMapper;
import com.xinyan.cashiersystem.model.User;
import com.xinyan.cashiersystem.model.product.Product;
import com.xinyan.cashiersystem.model.product.ProductParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: xinyan
 * @data: 2022/09/04/18:05
 **/

@Slf4j
@Service
public class ProductService {
    private final ProductMapper productMapper;

    @Autowired
    public ProductService(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public Product create(User user, ProductParam productParam) {
        Product product = new Product(user, productParam);
        productMapper.insert(product);

        return product;
    }

    public List<Product> getList() {
        return productMapper.selectAll();
    }

    public Product update(User user, ProductParam productParam) {
        Product product  = new Product(user, productParam);
        productMapper.updateByProductId(product);
        return product;
    }
}
