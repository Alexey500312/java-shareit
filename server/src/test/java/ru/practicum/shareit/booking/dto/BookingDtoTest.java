package ru.practicum.shareit.booking.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.context.annotation.Import;
import ru.practicum.shareit.TestData;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.user.UserMapper;

import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@Import({BookingMapper.class, UserMapper.class, ItemMapper.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingDtoTest {
    private final JacksonTester<BookingDto> json;
    private final BookingMapper bookingMapper;

    @Test
    @DisplayName("Сериализация")
    public void shouldSerialization() throws Exception {
        BookingDto bookingDto = bookingMapper.toBookingDto(TestData.getBookings().getFirst());
        JsonContent<BookingDto> result = json.write(bookingDto);

        assertThat(result).extractingJsonPathNumberValue("$.id")
                .isEqualTo(bookingDto.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.start")
                .isEqualTo(bookingDto.getStart().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        assertThat(result).extractingJsonPathStringValue("$.end")
                .isEqualTo(bookingDto.getEnd().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        assertThat(result).extractingJsonPathStringValue("$.status")
                .isEqualTo(bookingDto.getStatus().name());
        assertThat(result).extractingJsonPathNumberValue("$.item.id")
                .isEqualTo(bookingDto.getItem().getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.item.name")
                .isEqualTo(bookingDto.getItem().getName());
        assertThat(result).extractingJsonPathStringValue("$.item.description")
                .isEqualTo(bookingDto.getItem().getDescription());
        assertThat(result).extractingJsonPathNumberValue("$.item.owner.id")
                .isEqualTo(bookingDto.getItem().getOwner().getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.item.owner.name")
                .isEqualTo(bookingDto.getItem().getOwner().getName());
        assertThat(result).extractingJsonPathStringValue("$.item.owner.email")
                .isEqualTo(bookingDto.getItem().getOwner().getEmail());
        assertThat(result).extractingJsonPathNumberValue("$.booker.id")
                .isEqualTo(bookingDto.getBooker().getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.booker.name")
                .isEqualTo(bookingDto.getBooker().getName());
        assertThat(result).extractingJsonPathStringValue("$.booker.email")
                .isEqualTo(bookingDto.getBooker().getEmail());
    }

    @Test
    @DisplayName("Десериализация")
    public void shouldDeserialization() throws Exception {
        BookingDto bookingDto = bookingMapper.toBookingDto(TestData.getBookings().getFirst());
        String request = "{\n" +
                "\"id\": 1,\n" +
                "\"start\": \"2025-03-23T21:00:00\",\n" +
                "\"end\": \"2025-03-24T21:00:00\",\n" +
                "\"item\": {\n" +
                "\"id\": 1,\n" +
                "\"name\": \"name1\",\n" +
                "\"description\": \"description1\",\n" +
                "\"owner\": {\n" +
                "\"id\": 1,\n" +
                "\"name\": \"test1\",\n" +
                "\"email\": \"test1@yandex.ru\"\n" +
                "},\n" +
                "\"available\": true\n" +
                "},\n" +
                "\"booker\": {\n" +
                "\"id\": 2,\n" +
                "\"name\": \"test2\",\n" +
                "\"email\": \"test2@yandex.ru\"\n" +
                "},\n" +
                "\"status\": \"APPROVED\"\n" +
                "}";
        BookingDto result = json.parseObject(request);

        Assertions.assertEquals(result, bookingDto);
    }
}
