package ru.practicum.shareit.item.dto;

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
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RequestCommentDtoTest {
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
    @DisplayName("Текст комментария")
    public void shouldText() {
        RequestCommentDto requestCommentDto = RequestCommentDto.builder().build();

        List<ConstraintViolation<RequestCommentDto>> violations =
                new ArrayList<>(validator.validate(requestCommentDto, ValidatorGroups.Create.class));
        assertFalse(violations.isEmpty(), "Валидация пройдена");
        assertEquals("Текст комментария не может быть пустым", violations.getFirst().getMessage());

        requestCommentDto.setText("");
        violations = new ArrayList<>(validator.validate(requestCommentDto, ValidatorGroups.Create.class));
        assertFalse(violations.isEmpty(), "Валидация пройдена");
        assertEquals("Текст комментария не может быть пустым", violations.getFirst().getMessage());

        requestCommentDto.setText("test");
        violations = new ArrayList<>(validator.validate(requestCommentDto, ValidatorGroups.Create.class));
        assertTrue(violations.isEmpty(), "Валидация не пройдена");
    }
}
