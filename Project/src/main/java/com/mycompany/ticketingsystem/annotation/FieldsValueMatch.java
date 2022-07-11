package com.mycompany.ticketingsystem.annotation;

import com.mycompany.ticketingsystem.validation.FieldsValueMatchValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = FieldsValueMatchValidator.class) // Class that holds the logic
@Target({ElementType.TYPE}) // Applies to Class, interfaces, etc
@Retention(RetentionPolicy.RUNTIME) // Validation happens at runtime
public @interface FieldsValueMatch {
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String message() default "Field values don't match!";

    String field();

    String fieldMatch();

    @Target({ ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        FieldsValueMatch[] value();
    }
}
