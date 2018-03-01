package com.genie3.eventsLocation.constraints;


import com.genie3.eventsLocation.validators.AttemptAuthUserValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER , ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AttemptAuthUserValidator.class)
public @interface AttemptAuthUser {

    String message() default "Provide pseudo and password";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
