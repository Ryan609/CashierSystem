package com.xinyan.cashiersystem.mapper;

import com.xinyan.cashiersystem.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserMapper {
    void insert(User user);
}
