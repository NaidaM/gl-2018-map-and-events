package com.genie3.eventsLocation.validators;

import com.genie3.eventsLocation.constraints.AttemptAuthUser;
import com.genie3.eventsLocation.constraints.ValidPassword;
import com.genie3.eventsLocation.models.User;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class AttemptAuthUserValidator
        implements ConstraintValidator<AttemptAuthUser, User> {


    public boolean isValid(User u, ConstraintValidatorContext constraintValidatorContext) {

        if(u == null || u.getPseudo() == null || u.getPassword() == null){
            return false;
        }
        return true;
    }

    public void initialize(AttemptAuthUser ValidPassword) {

    }
}
