package com.xinyan.cashiersystem.model;

import com.xinyan.cashiersystem.controller.ErrorRedirectException;

/**
 * @author: xinyan
 * @data: 2022/09/04/16:14
 **/
// 这个类不准备实例化对象，所以定义成抽象类
public abstract class AbsValidator {
    public String validate(String module, String redirectUrl, String value) {
        if (value == null) {
            throw new ErrorRedirectException("value 是 null", module, redirectUrl);
        }

        value = value.trim();
        if (value.isEmpty()) {
            throw new ErrorRedirectException("value 是 \"\"", module, redirectUrl);
        }

        return value;
    }
}
