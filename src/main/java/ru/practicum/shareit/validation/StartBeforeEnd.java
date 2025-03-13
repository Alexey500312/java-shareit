package ru.practicum.shareit.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {StartBeforeEndValidator.class})
public @interface StartBeforeEnd {
    String message() default "{ru.practicum.shareit.validation.StartNotEqualEnd.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
