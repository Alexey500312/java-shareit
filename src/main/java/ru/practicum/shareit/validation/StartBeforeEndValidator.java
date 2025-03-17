package ru.practicum.shareit.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.practicum.shareit.booking.dto.StartEnd;

import java.time.LocalDateTime;

public class StartBeforeEndValidator implements ConstraintValidator<StartBeforeEnd, StartEnd> {
    @Override
    public boolean isValid(final StartEnd value, final ConstraintValidatorContext context) {
        final LocalDateTime start = value.getStart();
        final LocalDateTime end = value.getEnd();
        return start.isBefore(end);
    }

    @Override
    public void initialize(StartBeforeEnd constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}
