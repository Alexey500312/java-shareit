package ru.practicum.shareit.request.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.validation.ValidatorGroups;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RequestItemRequestDtoTest {
    private ValidatorFactory validatorFactory;
    private Validator validator;

    @BeforeEach
    public void init() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterEach
    public void close() {
        validatorFactory.close();
    }

    @Test
    @DisplayName("Описание запроса")
    public void shouldDescription() {
        RequestItemRequestDto requestItemRequestDto = RequestItemRequestDto.builder().build();

        List<ConstraintViolation<RequestItemRequestDto>> violations =
                new ArrayList<>(validator.validate(requestItemRequestDto, ValidatorGroups.Create.class));
        assertFalse(violations.isEmpty(), "Валидация пройдена");
        assertEquals("Текст описания не может быть пустым", violations.getFirst().getMessage());

        requestItemRequestDto.setDescription("");
        violations = new ArrayList<>(validator.validate(requestItemRequestDto, ValidatorGroups.Create.class));
        assertFalse(violations.isEmpty(), "Валидация пройдена");
        assertEquals("Текст описания не может быть пустым", violations.getFirst().getMessage());

        requestItemRequestDto.setDescription("test");
        violations = new ArrayList<>(validator.validate(requestItemRequestDto, ValidatorGroups.Create.class));
        assertTrue(violations.isEmpty(), "Валидация не пройдена");
    }
}
