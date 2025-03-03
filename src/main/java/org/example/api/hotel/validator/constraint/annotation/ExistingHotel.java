package org.example.api.hotel.validator.constraint.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.example.api.hotel.validator.constraint.ExistingHotelValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExistingHotelValidator.class)
public @interface ExistingHotel {
    String message() default "Hotel with this ID does not exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}