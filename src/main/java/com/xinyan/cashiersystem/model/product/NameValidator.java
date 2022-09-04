package com.xinyan.cashiersystem.model.product;

import com.xinyan.cashiersystem.controller.ErrorRedirectException;
import com.xinyan.cashiersystem.model.AbsValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NameValidator extends AbsValidator {
    @Override
    public String validate(String module, String redirectUrl, String name) {
        name = super.validate(module, redirectUrl, name);

        if (name.length() > 100) {
            throw new ErrorRedirectException("value 的长度超过 100", module, redirectUrl);
        }

        return name;
    }
}
