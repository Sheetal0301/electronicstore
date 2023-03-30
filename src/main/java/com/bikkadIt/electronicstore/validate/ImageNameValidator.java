package com.bikkadIt.electronicstore.validate;

import com.bikkadIt.electronicstore.controller.UserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ImageNameValidator implements ConstraintValidator<ImageNameValid,String> {
    Logger logger= LoggerFactory.getLogger(ImageNameValidator.class);
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        logger.info("Message from isValid : {} "+value);
        if(value.isBlank()){
            return  false;
        }
        return true;
    }
}
