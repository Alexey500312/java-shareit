package ru.practicum.shareit.request.dto;

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
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.user.UserMapper;

import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@Import({ItemRequestMapper.class, UserMapper.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestDtoTest {
    private final JacksonTester<ItemRequestDto> json;
    private final ItemRequestMapper itemRequestMapper;

    @Test
    @DisplayName("Сериализация")
    public void shouldSerialization() throws Exception {
        ItemRequestDto itemRequestDto = itemRequestMapper.toItemRequestDto(TestData.getItemRequest());
        JsonContent<ItemRequestDto> result = json.write(itemRequestDto);

        assertThat(result).extractingJsonPathNumberValue("$.id")
                .isEqualTo(itemRequestDto.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo(itemRequestDto.getDescription());
        assertThat(result).extractingJsonPathNumberValue("$.requestor.id")
                .isEqualTo(itemRequestDto.getRequestor().getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.requestor.name")
                .isEqualTo(itemRequestDto.getRequestor().getName());
        assertThat(result).extractingJsonPathStringValue("$.requestor.email")
                .isEqualTo(itemRequestDto.getRequestor().getEmail());
        assertThat(result).extractingJsonPathStringValue("$.created")
                .isEqualTo(itemRequestDto.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
    }

    @Test
    @DisplayName("Десериализация")
    public void shouldDeserialization() throws Exception {
        ItemRequestDto itemRequestDto = itemRequestMapper.toItemRequestDto(TestData.getItemRequest());
        String request = "{\n"
                + "\"id\": 1,\n"
                + "\"description\": \"description1\",\n"
                + "\"requestor\": {\n"
                + "\"id\": 2,\n"
                + "\"name\": \"test2\",\n"
                + "\"email\": \"test2@yandex.ru\"\n"
                + "},\n"
                + "\"created\": \"2025-03-24T21:24:16\"\n"
                + "}";
        ItemRequestDto result = json.parseObject(request);

        Assertions.assertEquals(result, itemRequestDto);
    }
}
