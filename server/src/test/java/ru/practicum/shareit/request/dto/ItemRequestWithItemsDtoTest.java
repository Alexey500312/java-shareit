package ru.practicum.shareit.request.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.TestData;

import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestWithItemsDtoTest {
    private final JacksonTester<ItemRequestWithItemsDto> json;

    @Test
    @DisplayName("Сериализация")
    public void shouldSerialization() throws Exception {
        ItemRequestWithItemsDto itemRequestWithItemsDto = TestData.getItemRequestWithItemsDto();
        JsonContent<ItemRequestWithItemsDto> result = json.write(itemRequestWithItemsDto);

        assertThat(result).extractingJsonPathNumberValue("$.id")
                .isEqualTo(itemRequestWithItemsDto.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo(itemRequestWithItemsDto.getDescription());
        assertThat(result).extractingJsonPathStringValue("$.created")
                .isEqualTo(itemRequestWithItemsDto.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        assertThat(result).extractingJsonPathNumberValue("$.items[0].id")
                .isEqualTo(itemRequestWithItemsDto.getItems().getFirst().getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.items[0].name")
                .isEqualTo(itemRequestWithItemsDto.getItems().getFirst().getName());
        assertThat(result).extractingJsonPathNumberValue("$.items[0].ownerId")
                .isEqualTo(itemRequestWithItemsDto.getItems().getFirst().getOwnerId().intValue());
    }

    @Test
    @DisplayName("Десериализация")
    public void shouldDeserialization() throws Exception {
        ItemRequestWithItemsDto itemRequestWithItemsDto = TestData.getItemRequestWithItemsDto();
        String request = "{\n"
                + "\"id\": 1,\n"
                + "\"description\": \"description1\",\n"
                + "\"created\": \"2025-03-24T21:24:16\",\n"
                + "\"items\": [\n"
                + "{\n"
                + "\"id\": 1,\n"
                + "\"name\": \"name1\",\n"
                + "\"ownerId\": 1\n"
                + "}\n"
                + "]\n"
                + "}";
        ItemRequestWithItemsDto result = json.parseObject(request);

        assertEquals(result, itemRequestWithItemsDto);
    }
}
