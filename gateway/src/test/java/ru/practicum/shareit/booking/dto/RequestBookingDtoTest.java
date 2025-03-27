package ru.practicum.shareit.booking.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.validation.ValidatorGroups;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RequestBookingDtoTest {
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
    @DisplayName("Вещь для бронирования")
    public void shouldItem() {
        RequestBookingDto requestBookingDto = RequestBookingDto.builder()
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();

        List<ConstraintViolation<RequestBookingDto>> violations =
                new ArrayList<>(validator.validate(requestBookingDto, ValidatorGroups.Create.class));
        assertFalse(violations.isEmpty(), "Валидация пройдена");
        assertEquals("Вещь не может быть пустой", violations.getFirst().getMessage());

        requestBookingDto.setItemId(1L);
        violations = new ArrayList<>(validator.validate(requestBookingDto, ValidatorGroups.Create.class));
        assertTrue(violations.isEmpty(), "Валидация не пройдена");
    }

    @Test
    @DisplayName("Дата и время начала бронирования")
    public void shouldStart() {
        RequestBookingDto requestBookingDto = RequestBookingDto.builder()
                .itemId(1L)
                .end(LocalDateTime.now().plusDays(2))
                .build();

        List<ConstraintViolation<RequestBookingDto>> violations =
                new ArrayList<>(validator.validate(requestBookingDto, ValidatorGroups.Create.class));
        assertFalse(violations.isEmpty(), "Валидация пройдена");
        assertEquals("Дата начала бронирования не может быть пустой", violations.getFirst().getMessage());

        requestBookingDto.setStart(LocalDateTime.now().plusDays(-1));
        violations = new ArrayList<>(validator.validate(requestBookingDto, ValidatorGroups.Create.class));
        assertFalse(violations.isEmpty(), "Валидация пройдена");
        assertEquals(
                "Дата начала бронирования не может быть меньше текущей даты и времени",
                violations.getFirst().getMessage());

        requestBookingDto.setStart(LocalDateTime.now().plusDays(1));
        violations = new ArrayList<>(validator.validate(requestBookingDto, ValidatorGroups.Create.class));
        assertTrue(violations.isEmpty(), "Валидация не пройдена");
    }

    @Test
    @DisplayName("Дата и время окончания бронирования")
    public void shouldEnd() {
        RequestBookingDto requestBookingDto = RequestBookingDto.builder()
                .itemId(1L)
                .start(LocalDateTime.now().plusDays(1))
                .build();

        List<ConstraintViolation<RequestBookingDto>> violations =
                new ArrayList<>(validator.validate(requestBookingDto, ValidatorGroups.Create.class));
        assertFalse(violations.isEmpty(), "Валидация пройдена");
        assertEquals("Дата окончания бронирования не может быть пустой", violations.getFirst().getMessage());

        requestBookingDto.setEnd(LocalDateTime.now());
        violations = new ArrayList<>(validator.validate(requestBookingDto, ValidatorGroups.Create.class));
        assertFalse(violations.isEmpty(), "Валидация пройдена");
        assertEquals(
                "Дата окончания бронирования не может быть меньше текущей даты и времени",
                violations.getFirst().getMessage());

        requestBookingDto.setEnd(LocalDateTime.now().plusDays(2));
        violations = new ArrayList<>(validator.validate(requestBookingDto, ValidatorGroups.Create.class));
        assertTrue(violations.isEmpty(), "Валидация не пройдена");
    }

    @Test
    @DisplayName("Сравнение даты начала и даты окончания")
    public void shouldStartBeforeEnd() {
        RequestBookingDto requestBookingDto = RequestBookingDto.builder()
                .itemId(1L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now())
                .build();

        List<ConstraintViolation<RequestBookingDto>> violations =
                new ArrayList<>(validator.validate(requestBookingDto));
        assertFalse(violations.isEmpty(), "Валидация пройдена");
        assertEquals(
                "Дата окончания бронирования должна быть больше даты начала",
                violations.getFirst().getMessage());

        requestBookingDto.setEnd(LocalDateTime.now().plusDays(2));
        violations = new ArrayList<>(validator.validate(requestBookingDto));
        assertTrue(violations.isEmpty(), "Валидация не пройдена");
    }
}
