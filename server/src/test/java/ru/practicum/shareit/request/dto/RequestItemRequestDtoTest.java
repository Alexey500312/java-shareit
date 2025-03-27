package ru.practicum.shareit.request.dto;

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
public class RequestItemRequestDtoTest {
    private final JacksonTester<RequestItemRequestDto> json;

    @Test
    @DisplayName("Сериализация")
    public void shouldSerialization() throws Exception {
        RequestItemRequestDto requestItemRequestDto = new RequestItemRequestDto(TestData.getItemRequest().getDescription());
        JsonContent<RequestItemRequestDto> result = json.write(requestItemRequestDto);

        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo(requestItemRequestDto.getDescription());
    }

    @Test
    @DisplayName("Десериализация")
    public void shouldDeserialization() throws Exception {
        RequestItemRequestDto requestItemRequestDto = new RequestItemRequestDto(TestData.getItemRequest().getDescription());
        String request = "{"
                + "\"description\": \"description1\""
                + "}";
        RequestItemRequestDto result = json.parseObject(request);

        assertEquals(result, requestItemRequestDto);
    }
}
