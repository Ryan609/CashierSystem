package com.xinyan.cashiersystem.model;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: xinyan
 * @data: 2022/09/01/21:55
 **/

@Slf4j
@Data
public class User {
    private Integer userId;
    private String username;
    private String password;

    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
