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
public class ItemByRequestDtoTest {
    private final JacksonTester<ItemByRequestDto> json;

    @Test
    @DisplayName("Сериализация")
    public void shouldSerialization() throws Exception {
        Item item = TestData.getItem();
        ItemByRequestDto itemByRequestDto = new ItemByRequestDto(item.getId(), item.getName(), item.getOwner().getId());
        JsonContent<ItemByRequestDto> result = json.write(itemByRequestDto);

        assertThat(result).extractingJsonPathNumberValue("$.id")
                .isEqualTo(itemByRequestDto.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.name")
                .isEqualTo(itemByRequestDto.getName());
        assertThat(result).extractingJsonPathNumberValue("$.ownerId")
                .isEqualTo(itemByRequestDto.getOwnerId().intValue());
    }

    @Test
    @DisplayName("Десериализация")
    public void shouldDeserialization() throws Exception {
        Item item = TestData.getItem();
        ItemByRequestDto itemByRequestDto = new ItemByRequestDto(item.getId(), item.getName(), item.getOwner().getId());
        String request = "{\n" +
                "\"id\": 1,\n" +
                "\"name\": \"name1\",\n" +
                "\"ownerId\": 1\n" +
                "}";
        ItemByRequestDto result = json.parseObject(request);

        assertEquals(result, itemByRequestDto);
    }
}
