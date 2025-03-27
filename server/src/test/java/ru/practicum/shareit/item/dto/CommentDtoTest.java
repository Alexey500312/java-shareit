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
public class CommentDtoTest {
    private final JacksonTester<CommentDto> json;

    @Test
    @DisplayName("Сериализация")
    public void shouldSerialization() throws Exception {
        CommentDto commentDto = TestData.getCommentDto(TestData.getComment());
        JsonContent<CommentDto> result = json.write(commentDto);

        assertThat(result).extractingJsonPathNumberValue("$.id")
                .isEqualTo(commentDto.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.text")
                .isEqualTo(commentDto.getText());
        assertThat(result).extractingJsonPathStringValue("$.authorName")
                .isEqualTo(commentDto.getAuthorName());
        assertThat(result).extractingJsonPathStringValue("$.created")
                .isEqualTo(commentDto.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
    }

    @Test
    @DisplayName("Десериализация")
    public void shouldDeserialization() throws Exception {
        CommentDto commentDto = TestData.getCommentDto(TestData.getComment());
        String request = "{\n" +
                "\"id\": 1,\n" +
                "\"text\": \"text1\",\n" +
                "\"authorName\": \"test2\",\n" +
                "\"created\": \"2025-03-25T12:00:00\"\n" +
                "}";
        CommentDto result = json.parseObject(request);

        assertEquals(result, commentDto);
    }
}
