package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.context.annotation.Import;
import ru.practicum.shareit.TestData;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.user.UserMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JsonTest
@Import({ItemMapper.class, UserMapper.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemDtoTest {
    private final JacksonTester<ItemDto> json;
    private final ItemMapper itemMapper;

    @Test
    @DisplayName("Сериализация")
    public void shouldSerialization() throws Exception {
        ItemDto itemDto = itemMapper.toItemDto(TestData.getItem());
        JsonContent<ItemDto> result = json.write(itemDto);

        assertThat(result).extractingJsonPathNumberValue("$.id")
                .isEqualTo(itemDto.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.name")
                .isEqualTo(itemDto.getName());
        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo(itemDto.getDescription());
        assertThat(result).extractingJsonPathNumberValue("$.owner.id")
                .isEqualTo(itemDto.getOwner().getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.owner.name")
                .isEqualTo(itemDto.getOwner().getName());
        assertThat(result).extractingJsonPathStringValue("$.owner.email")
                .isEqualTo(itemDto.getOwner().getEmail());
        assertThat(result).extractingJsonPathBooleanValue("$.available")
                .isEqualTo(itemDto.getAvailable());
    }

    @Test
    @DisplayName("Десериализация")
    public void shouldDeserialization() throws Exception {
        ItemDto itemDto = itemMapper.toItemDto(TestData.getItem());
        String request = "{\n" +
                "\"id\": 1,\n" +
                "\"name\": \"name1\",\n" +
                "\"description\": \"description1\",\n" +
                "\"owner\": {\n" +
                "\"id\": 1,\n" +
                "\"name\": \"test1\",\n" +
                "\"email\": \"test1@yandex.ru\"\n" +
                "},\n" +
                "\"available\": true\n" +
                "}";
        ItemDto result = json.parseObject(request);

        assertEquals(result, itemDto);
    }
}