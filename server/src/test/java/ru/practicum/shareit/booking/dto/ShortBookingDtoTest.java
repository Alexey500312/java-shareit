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

import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ShortBookingDtoTest {
    private final JacksonTester<ShortBookingDto> json;

    @Test
    @DisplayName("Сериализация")
    public void shouldSerialization() throws Exception {
        ShortBookingDto shortBookingDto = TestData.getShortBookingDto(TestData.getBookings().getFirst());
        JsonContent<ShortBookingDto> result = json.write(shortBookingDto);

        assertThat(result).extractingJsonPathNumberValue("$.id")
                .isEqualTo(shortBookingDto.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.start")
                .isEqualTo(shortBookingDto.getStart().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        assertThat(result).extractingJsonPathStringValue("$.end")
                .isEqualTo(shortBookingDto.getEnd().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        assertThat(result).extractingJsonPathStringValue("$.bookerName")
                .isEqualTo(shortBookingDto.getBookerName());
    }

    @Test
    @DisplayName("Десериализация")
    public void shouldDeserialization() throws Exception {
        ShortBookingDto shortBookingDto = TestData.getShortBookingDto(TestData.getBookings().getFirst());
        String request = "{\n" +
                "\"id\": 1,\n" +
                "\"start\": \"2025-03-23T21:00:00\",\n" +
                "\"end\": \"2025-03-24T21:00:00\",\n" +
                "\"bookerName\": \"test2\"\n" +
                "}";
        ShortBookingDto result = json.parseObject(request);

        Assertions.assertEquals(result, shortBookingDto);
    }
}
