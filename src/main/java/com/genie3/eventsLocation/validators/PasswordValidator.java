package com.genie3.eventsLocation.validators;

import com.genie3.eventsLocation.constraints.ValidPassword;
import com.genie3.eventsLocation.entities.User;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class PasswordValidator
        implements ConstraintValidator<ValidPassword, User> {


    public boolean isValid(User u, ConstraintValidatorContext constraintValidatorContext) {

        if(u == null || u.getPassword() == null || u.getPasswordConfirmation() == null){
            return false;
        }
        if(!u.getPassword().equals(u.getPasswordConfirmation())){
            return false;
        }
        return true;
    }

    public void initialize(ValidPassword ValidPassword) {

    }
}
