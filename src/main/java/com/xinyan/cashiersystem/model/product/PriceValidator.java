package com.xinyan.cashiersystem.model.product;

import com.xinyan.cashiersystem.controller.ErrorRedirectException;
import com.xinyan.cashiersystem.model.AbsValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PriceValidator extends AbsValidator {
    @Override
    public String validate(String module, String redirectUrl, String name) {
        name = super.validate(module, redirectUrl, name);

        // 得确认下 price 是不是数字，并且得是 > 0 的数字
        try {
            double number = Double.parseDouble(name);
            if (number <= 0) {
                throw new ErrorRedirectException("value 必须大于 0", module, redirectUrl);
            }
        } catch (NumberFormatException exc) {
            // 说明传入的 stock 无法转成数字
            throw new ErrorRedirectException("value 必须是数字", module, redirectUrl);
        }

        return name;
    }
}
