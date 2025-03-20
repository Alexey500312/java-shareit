package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.TestData;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestCommentDtoTest {
    private final JacksonTester<RequestCommentDto> json;

    @Test
    @DisplayName("Сериализация")
    public void shouldSerialization() throws Exception {
        RequestCommentDto requestCommentDto = new RequestCommentDto(TestData.getComment().getText());
        JsonContent<RequestCommentDto> result = json.write(requestCommentDto);

        assertThat(result).extractingJsonPathStringValue("$.text")
                .isEqualTo(requestCommentDto.getText());
    }

    @Test
    @DisplayName("Десериализация")
    public void shouldDeserialization() throws Exception {
        RequestCommentDto requestCommentDto = new RequestCommentDto(TestData.getComment().getText());
        String request = "{\n" +
                "\"text\": \"text1\"\n" +
                "}";
        RequestCommentDto result = json.parseObject(request);

        assertEquals(result, requestCommentDto);
    }
}
