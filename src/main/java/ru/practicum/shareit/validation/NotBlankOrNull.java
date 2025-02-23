package ru.practicum.shareit.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {NotBlankOrNullValidator.class})
public @interface NotBlankOrNull {
    String message() default "{jakarta.validation.constraints.NotBlankOrNull.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
