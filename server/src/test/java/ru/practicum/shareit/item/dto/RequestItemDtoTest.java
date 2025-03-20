package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.TestData;
import ru.practicum.shareit.item.model.Item;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestItemDtoTest {
    private final JacksonTester<RequestItemDto> json;

    @Test
    @DisplayName("Сериализация")
    public void shouldSerialization() throws Exception {
        Item item = TestData.getItem();
        RequestItemDto requestItemDto = new RequestItemDto(
                item.getName(),
                item.getDescription(),
                item.isAvailable(),
                item.getRequest().getId());
        JsonContent<RequestItemDto> result = json.write(requestItemDto);

        assertThat(result).extractingJsonPathStringValue("$.name")
                .isEqualTo(requestItemDto.getName());
        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo(requestItemDto.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.available")
                .isEqualTo(requestItemDto.getAvailable());
        assertThat(result).extractingJsonPathNumberValue("$.requestId")
                .isEqualTo(requestItemDto.getRequestId().intValue());
    }

    @Test
    @DisplayName("Десериализация")
    public void shouldDeserialization() throws Exception {
        Item item = TestData.getItem();
        RequestItemDto requestItemDto = new RequestItemDto(
                item.getName(),
                item.getDescription(),
                item.isAvailable(),
                item.getRequest().getId());
        String request = "{\n" +
                "\"name\": \"name1\",\n" +
                "\"description\": \"description1\",\n" +
                "\"available\": true,\n" +
                "\"requestId\": 1\n" +
                "}";
        RequestItemDto result = json.parseObject(request);

        assertEquals(result, requestItemDto);
    }
}
