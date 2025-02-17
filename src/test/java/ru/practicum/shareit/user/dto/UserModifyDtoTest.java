package ru.practicum.shareit.user.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemModifyDto;
import ru.practicum.shareit.validation.ValidatorGroups;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserModifyDtoTest {
    ValidatorFactory validatorFactory;
    Validator validator;

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
        UserModifyDto userModifyDto = UserModifyDto.builder()
                .email("test@yandex.ru")
                .build();

        List<ConstraintViolation<UserModifyDto>> violations =
                new ArrayList<>(validator.validate(userModifyDto, ValidatorGroups.Create.class));
        assertFalse(violations.isEmpty(), "Валидация пройдена");
        assertEquals("Имя не может быть пустым", violations.getFirst().getMessage());

        violations = new ArrayList<>(validator.validate(userModifyDto, ValidatorGroups.Update.class));
        assertTrue(violations.isEmpty(), "Валидация не пройдена");

        userModifyDto.setName("");
        violations = new ArrayList<>(validator.validate(userModifyDto, ValidatorGroups.Create.class));
        assertFalse(violations.isEmpty(), "Валидация пройдена");
        assertEquals("Имя не может быть пустым", violations.getFirst().getMessage());

        violations = new ArrayList<>(validator.validate(userModifyDto, ValidatorGroups.Update.class));
        assertFalse(violations.isEmpty(), "Валидация пройдена");
        assertEquals("Имя не может быть пустым", violations.getFirst().getMessage());

        userModifyDto.setName("test");
        violations = new ArrayList<>(validator.validate(userModifyDto, ValidatorGroups.Create.class));
        assertTrue(violations.isEmpty(), "Валидация не пройдена");

        violations = new ArrayList<>(validator.validate(userModifyDto, ValidatorGroups.Update.class));
        assertTrue(violations.isEmpty(), "Валидация не пройдена");
    }

    @Test
    @DisplayName("Электронная почта пользователя")
    public void shouldEmail() {
        UserModifyDto userModifyDto = UserModifyDto.builder()
                .name("name")
                .build();

        List<ConstraintViolation<UserModifyDto>> violations = new ArrayList<>(validator.validate(userModifyDto, ValidatorGroups.Create.class));
        assertFalse(violations.isEmpty(), "Валидация пройдена");
        assertEquals("Электронная почта не может быть пустой", violations.getFirst().getMessage());

        violations = new ArrayList<>(validator.validate(userModifyDto, ValidatorGroups.Update.class));
        assertTrue(violations.isEmpty(), "Валидация не пройдена");

        userModifyDto.setEmail("");
        violations = new ArrayList<>(validator.validate(userModifyDto, ValidatorGroups.Create.class));
        assertFalse(violations.isEmpty(), "Валидация пройдена");
        assertEquals("Электронная почта не может быть пустой", violations.getFirst().getMessage());

        violations = new ArrayList<>(validator.validate(userModifyDto, ValidatorGroups.Update.class));
        assertFalse(violations.isEmpty(), "Валидация пройдена");
        assertEquals("Электронная почта не может быть пустой", violations.getFirst().getMessage());

        userModifyDto.setEmail("yandex.ru");
        violations = new ArrayList<>(validator.validate(userModifyDto, ValidatorGroups.Create.class));
        assertFalse(violations.isEmpty(), "Валидация пройдена");
        assertEquals("Электронная почта не соответствует формату", violations.getFirst().getMessage());

        violations = new ArrayList<>(validator.validate(userModifyDto, ValidatorGroups.Update.class));
        assertFalse(violations.isEmpty(), "Валидация пройдена");
        assertEquals("Электронная почта не соответствует формату", violations.getFirst().getMessage());

        userModifyDto.setEmail("test@yandex.ru");
        violations = new ArrayList<>(validator.validate(userModifyDto, ValidatorGroups.Create.class));
        assertTrue(violations.isEmpty(), "Валидация пройдена");

        violations = new ArrayList<>(validator.validate(userModifyDto, ValidatorGroups.Update.class));
        assertTrue(violations.isEmpty(), "Валидация пройдена");
    }
}
