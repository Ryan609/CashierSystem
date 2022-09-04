package com.xinyan.cashiersystem.model.product;


import com.xinyan.cashiersystem.controller.ErrorRedirectException;
import com.xinyan.cashiersystem.model.AbsValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class IntroduceValidator extends AbsValidator {
    @Override
    public String validate(String module, String redirectUrl, String name) {
        name = super.validate(module, redirectUrl, name);

        if (name.length() > 200) {
            throw new ErrorRedirectException("value 的长度超过 200", module, redirectUrl);
        }

        return name;
    }
}
