package ru.practicum.shareit.booking.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.TestData;
import ru.practicum.shareit.booking.model.Booking;

import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestBookingDtoTest {
    private final JacksonTester<RequestBookingDto> json;

    @Test
    @DisplayName("Сериализация")
    public void shouldSerialization() throws Exception {
        Booking booking = TestData.getBookings().getFirst();
        RequestBookingDto requestBookingDto = new RequestBookingDto(
                booking.getId(),
                booking.getItem().getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getBooker().getId());
        JsonContent<RequestBookingDto> result = json.write(requestBookingDto);

        assertThat(result).extractingJsonPathNumberValue("$.id")
                .isEqualTo(requestBookingDto.getId().intValue());
        assertThat(result).extractingJsonPathNumberValue("$.itemId")
                .isEqualTo(requestBookingDto.getItemId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.start")
                .isEqualTo(requestBookingDto.getStart().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        assertThat(result).extractingJsonPathStringValue("$.end")
                .isEqualTo(requestBookingDto.getEnd().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        assertThat(result).extractingJsonPathNumberValue("$.bookerId")
                .isEqualTo(requestBookingDto.getBookerId().intValue());
    }

    @Test
    @DisplayName("Десериализация")
    public void shouldDeserialization() throws Exception {
        Booking booking = TestData.getBookings().getFirst();
        RequestBookingDto requestBookingDto = new RequestBookingDto(
                booking.getId(),
                booking.getItem().getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getBooker().getId());
        String request = "{\n" +
                "\"id\": 1,\n" +
                "\"itemId\": 1,\n" +
                "\"start\": \"2025-03-23T21:00:00\",\n" +
                "\"end\": \"2025-03-24T21:00:00\",\n" +
                "\"bookerId\": 2\n" +
                "}";
        RequestBookingDto result = json.parseObject(request);

        Assertions.assertEquals(result, requestBookingDto);
    }
}
