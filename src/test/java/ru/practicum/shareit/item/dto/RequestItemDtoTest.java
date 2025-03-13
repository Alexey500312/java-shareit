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

public class RequestItemDtoTest {
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
    @DisplayName("Имя вещи")
    public void shouldName() {
        RequestItemDto itemModifyDto = RequestItemDto.builder()
                .description("test")
                .available(true)
                .build();

        List<ConstraintViolation<RequestItemDto>> violations = new ArrayList<>(validator.validate(itemModifyDto, ValidatorGroups.Create.class));
        assertFalse(violations.isEmpty(), "Валидация пройдена");
        assertEquals("Имя не может быть пустым", violations.getFirst().getMessage());

        violations = new ArrayList<>(validator.validate(itemModifyDto, ValidatorGroups.Update.class));
        assertTrue(violations.isEmpty(), "Валидация не пройдена");

        itemModifyDto.setName("");
        violations = new ArrayList<>(validator.validate(itemModifyDto, ValidatorGroups.Create.class));
        assertFalse(violations.isEmpty(), "Валидация пройдена");
        assertEquals("Имя не может быть пустым", violations.getFirst().getMessage());

        violations = new ArrayList<>(validator.validate(itemModifyDto, ValidatorGroups.Update.class));
        assertFalse(violations.isEmpty(), "Валидация пройдена");
        assertEquals("Имя не может быть пустым", violations.getFirst().getMessage());

        itemModifyDto.setName("test");
        violations = new ArrayList<>(validator.validate(itemModifyDto, ValidatorGroups.Create.class));
        assertTrue(violations.isEmpty(), "Валидация не пройдена");

        violations = new ArrayList<>(validator.validate(itemModifyDto, ValidatorGroups.Update.class));
        assertTrue(violations.isEmpty(), "Валидация не пройдена");
    }

    @Test
    @DisplayName("Описание вещи")
    public void shouldescription() {
        RequestItemDto itemModifyDto = RequestItemDto.builder()
                .name("test")
                .available(true)
                .build();

        List<ConstraintViolation<RequestItemDto>> violations =
                new ArrayList<>(validator.validate(itemModifyDto, ValidatorGroups.Create.class));
        assertFalse(violations.isEmpty(), "Валидация пройдена");
        assertEquals("Описание не может быть пустым", violations.getFirst().getMessage());

        violations = new ArrayList<>(validator.validate(itemModifyDto, ValidatorGroups.Update.class));
        assertTrue(violations.isEmpty(), "Валидация не пройдена");

        itemModifyDto.setDescription("");
        violations = new ArrayList<>(validator.validate(itemModifyDto, ValidatorGroups.Create.class));
        assertFalse(violations.isEmpty(), "Валидация пройдена");
        assertEquals("Описание не может быть пустым", violations.getFirst().getMessage());

        violations = new ArrayList<>(validator.validate(itemModifyDto, ValidatorGroups.Update.class));
        assertFalse(violations.isEmpty(), "Валидация пройдена");
        assertEquals("Описание не может быть пустым", violations.getFirst().getMessage());

        itemModifyDto.setDescription("test");
        violations = new ArrayList<>(validator.validate(itemModifyDto, ValidatorGroups.Create.class));
        assertTrue(violations.isEmpty(), "Валидация не пройдена");

        violations = new ArrayList<>(validator.validate(itemModifyDto, ValidatorGroups.Update.class));
        assertTrue(violations.isEmpty(), "Валидация не пройдена");
    }

    @Test
    @DisplayName("Статус доступности вещи для аренды")
    public void shouldAvailable() {
        RequestItemDto itemModifyDto = RequestItemDto.builder()
                .name("test")
                .description("test")
                .build();

        List<ConstraintViolation<RequestItemDto>> violations =
                new ArrayList<>(validator.validate(itemModifyDto, ValidatorGroups.Create.class));
        assertFalse(violations.isEmpty(), "Валидация пройдена");
        assertEquals("Статус доступности вещи для аренды не может быть пустым", violations.getFirst().getMessage());

        violations = new ArrayList<>(validator.validate(itemModifyDto, ValidatorGroups.Update.class));
        assertTrue(violations.isEmpty(), "Валидация не пройдена");

        itemModifyDto.setAvailable(true);
        violations = new ArrayList<>(validator.validate(itemModifyDto, ValidatorGroups.Create.class));
        assertTrue(violations.isEmpty(), "Валидация не пройдена");

        violations = new ArrayList<>(validator.validate(itemModifyDto, ValidatorGroups.Update.class));
        assertTrue(violations.isEmpty(), "Валидация не пройдена");
    }
}
