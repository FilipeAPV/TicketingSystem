package com.mycompany.ticketingsystem.annotation;

import com.mycompany.ticketingsystem.validation.EmailMustBeUniqueValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmailMustBeUniqueValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailMustBeUnique {
    String message() default "There is already a colleague registered with the same Email. Please choose a different one.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
