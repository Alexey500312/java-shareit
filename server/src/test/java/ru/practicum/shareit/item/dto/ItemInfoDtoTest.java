package ru.practicum.shareit.item.dto;

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
public class ItemInfoDtoTest {
    private final JacksonTester<ItemInfoDto> json;

    @Test
    @DisplayName("Сериализация")
    public void shouldSerialization() throws Exception {
        ItemInfoDto itemInfoDto = TestData.getItemInfoDto();
        JsonContent<ItemInfoDto> result = json.write(itemInfoDto);

        assertThat(result).extractingJsonPathNumberValue("$.id")
                .isEqualTo(itemInfoDto.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.name")
                .isEqualTo(itemInfoDto.getName());
        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo(itemInfoDto.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.available")
                .isEqualTo(itemInfoDto.getAvailable());
        assertThat(result).extractingJsonPathNumberValue("$.lastBooking.id")
                .isEqualTo(itemInfoDto.getLastBooking().getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.lastBooking.start")
                .isEqualTo(itemInfoDto.getLastBooking().getStart()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        assertThat(result).extractingJsonPathStringValue("$.lastBooking.end")
                .isEqualTo(itemInfoDto.getLastBooking().getEnd()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        assertThat(result).extractingJsonPathNumberValue("$.nextBooking.id")
                .isEqualTo(itemInfoDto.getNextBooking().getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.nextBooking.start")
                .isEqualTo(itemInfoDto.getNextBooking().getStart()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        assertThat(result).extractingJsonPathStringValue("$.nextBooking.end")
                .isEqualTo(itemInfoDto.getNextBooking().getEnd()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        assertThat(result).extractingJsonPathNumberValue("$.comments[0].id")
                .isEqualTo(itemInfoDto.getComments().getFirst().getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.comments[0].text")
                .isEqualTo(itemInfoDto.getComments().getFirst().getText());
        assertThat(result).extractingJsonPathStringValue("$.comments[0].authorName")
                .isEqualTo(itemInfoDto.getComments().getFirst().getAuthorName());
        assertThat(result).extractingJsonPathStringValue("$.comments[0].created")
                .isEqualTo(itemInfoDto.getComments().getFirst().getCreated()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
    }

    @Test
    @DisplayName("Десериализация")
    public void shouldDeserialization() throws Exception {
        ItemInfoDto itemInfoDto = TestData.getItemInfoDto();
        String request = "{\n" +
                "\"id\": 1,\n" +
                "\"name\": \"name1\",\n" +
                "\"description\": \"description1\",\n" +
                "\"available\": true,\n" +
                "\"lastBooking\": {\n" +
                "\"id\": 1,\n" +
                "\"start\": \"2025-03-23T21:00:00\",\n" +
                "\"end\": \"2025-03-24T21:00:00\",\n" +
                "\"bookerName\": \"test2\"\n" +
                "},\n" +
                "\"nextBooking\": {\n" +
                "\"id\": 2,\n" +
                "\"start\": \"2025-04-23T21:00:00\",\n" +
                "\"end\": \"2025-04-24T21:00:00\",\n" +
                "\"bookerName\": \"test2\"\n" +
                "},\n" +
                "\"comments\": [\n" +
                "{\n" +
                "\"id\": 1,\n" +
                "\"text\": \"text1\",\n" +
                "\"authorName\": \"test2\",\n" +
                "\"created\": \"2025-03-25T12:00:00\"\n" +
                "}\n" +
                "]\n" +
                "}";
        ItemInfoDto result = json.parseObject(request);

        assertEquals(result, itemInfoDto);
    }
}
