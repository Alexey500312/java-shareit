package ru.practicum.shareit.user.dto;

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

public class RequestUserDtoTest {
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
    @DisplayName("Имя пользователя")
    public void shouldName() {
        RequestUserDto requestUserDto = RequestUserDto.builder()
                .email("test@yandex.ru")
                .build();

        List<ConstraintViolation<RequestUserDto>> violations =
                new ArrayList<>(validator.validate(requestUserDto, ValidatorGroups.Create.class));
        assertFalse(violations.isEmpty(), "Валидация пройдена");
        assertEquals("Имя не может быть пустым", violations.getFirst().getMessage());

        violations = new ArrayList<>(validator.validate(requestUserDto, ValidatorGroups.Update.class));
        assertTrue(violations.isEmpty(), "Валидация не пройдена");

        requestUserDto.setName("");
        violations = new ArrayList<>(validator.validate(requestUserDto, ValidatorGroups.Create.class));
        assertFalse(violations.isEmpty(), "Валидация пройдена");
        assertEquals("Имя не может быть пустым", violations.getFirst().getMessage());

        violations = new ArrayList<>(validator.validate(requestUserDto, ValidatorGroups.Update.class));
        assertFalse(violations.isEmpty(), "Валидация пройдена");
        assertEquals("Имя не может быть пустым", violations.getFirst().getMessage());

        requestUserDto.setName("test");
        violations = new ArrayList<>(validator.validate(requestUserDto, ValidatorGroups.Create.class));
        assertTrue(violations.isEmpty(), "Валидация не пройдена");

        violations = new ArrayList<>(validator.validate(requestUserDto, ValidatorGroups.Update.class));
        assertTrue(violations.isEmpty(), "Валидация не пройдена");
    }

    @Test
    @DisplayName("Электронная почта пользователя")
    public void shouldEmail() {
        RequestUserDto requestUserDto = RequestUserDto.builder()
                .name("name")
                .build();

        List<ConstraintViolation<RequestUserDto>> violations = new ArrayList<>(validator.validate(requestUserDto, ValidatorGroups.Create.class));
        assertFalse(violations.isEmpty(), "Валидация пройдена");
        assertEquals("Электронная почта не может быть пустой", violations.getFirst().getMessage());

        violations = new ArrayList<>(validator.validate(requestUserDto, ValidatorGroups.Update.class));
        assertTrue(violations.isEmpty(), "Валидация не пройдена");

        requestUserDto.setEmail("");
        violations = new ArrayList<>(validator.validate(requestUserDto, ValidatorGroups.Create.class));
        assertFalse(violations.isEmpty(), "Валидация пройдена");
        assertEquals("Электронная почта не может быть пустой", violations.getFirst().getMessage());

        violations = new ArrayList<>(validator.validate(requestUserDto, ValidatorGroups.Update.class));
        assertFalse(violations.isEmpty(), "Валидация пройдена");
        assertEquals("Электронная почта не может быть пустой", violations.getFirst().getMessage());

        requestUserDto.setEmail("yandex.ru");
        violations = new ArrayList<>(validator.validate(requestUserDto, ValidatorGroups.Create.class));
        assertFalse(violations.isEmpty(), "Валидация пройдена");
        assertEquals("Электронная почта не соответствует формату", violations.getFirst().getMessage());

        violations = new ArrayList<>(validator.validate(requestUserDto, ValidatorGroups.Update.class));
        assertFalse(violations.isEmpty(), "Валидация пройдена");
        assertEquals("Электронная почта не соответствует формату", violations.getFirst().getMessage());

        requestUserDto.setEmail("test@yandex.ru");
        violations = new ArrayList<>(validator.validate(requestUserDto, ValidatorGroups.Create.class));
        assertTrue(violations.isEmpty(), "Валидация пройдена");

        violations = new ArrayList<>(validator.validate(requestUserDto, ValidatorGroups.Update.class));
        assertTrue(violations.isEmpty(), "Валидация пройдена");
    }
}
