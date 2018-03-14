package com.genie3.eventsLocation.validators;

import com.genie3.eventsLocation.constraints.ValidTag;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class TagValidator
        implements ConstraintValidator<ValidTag, Object> {


    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {

        try {
           String [] tag =  (String []) o;
           return true;
        }catch (Exception ex){
            return false;
        }
    }

    public void initialize(ValidTag ValidPassword) {

    }
}
