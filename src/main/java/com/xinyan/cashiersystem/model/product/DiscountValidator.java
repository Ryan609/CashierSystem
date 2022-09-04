package com.xinyan.cashiersystem.model.product;

import com.xinyan.cashiersystem.controller.ErrorRedirectException;
import com.xinyan.cashiersystem.model.AbsValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DiscountValidator extends AbsValidator {
    @Override
    public String validate(String module, String redirectUrl, String name) {
        name = super.validate(module, redirectUrl, name);

        // 得确认下 price 是不是数字，并且得是 > 0 的数字
        try {
            int number = Integer.parseInt(name);
            if (number <= 0 || number > 100) {
                throw new ErrorRedirectException("value 必须是 1 到 100 的范围内", module, redirectUrl);
            }
        } catch (NumberFormatException exc) {
            // 说明传入的 stock 无法转成数字
            throw new ErrorRedirectException("value 必须是数字", module, redirectUrl);
        }

        return name;
    }
}
