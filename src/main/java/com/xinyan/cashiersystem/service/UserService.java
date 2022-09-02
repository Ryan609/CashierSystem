package com.xinyan.cashiersystem.service;

import com.xinyan.cashiersystem.mapper.UserMapper;
import com.xinyan.cashiersystem.model.User;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: xinyan
 * @data: 2022/09/02/21:55
 **/

@Slf4j
@Service
public class UserService {

    private final UserMapper userMapper;

    @Autowired
    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }


    public User register(String username, String password) {
        // 对密码进行加密处理
        String salt = BCrypt.gensalt();
        String hashPassword = BCrypt.hashpw(password, salt);

        // 进行插入操作
        User user = new User(username, password);
        userMapper.insert(user);

        return user;
    }
}
