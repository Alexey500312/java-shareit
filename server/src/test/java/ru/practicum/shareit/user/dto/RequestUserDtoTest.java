package ru.practicum.shareit.user.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.TestData;
import ru.practicum.shareit.user.model.User;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestUserDtoTest {
    private final JacksonTester<RequestUserDto> json;

    @Test
    @DisplayName("Сериализация")
    public void shouldSerialization() throws Exception {
        User user = TestData.getUser();
        RequestUserDto requestUserDto = new RequestUserDto(user.getName(), user.getEmail());
        JsonContent<RequestUserDto> result = json.write(requestUserDto);

        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(requestUserDto.getName());
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo(requestUserDto.getEmail());
    }

    @Test
    @DisplayName("Десериализация")
    public void shouldDeserialization() throws Exception {
        User user = TestData.getUser();
        RequestUserDto requestUserDto = new RequestUserDto(user.getName(), user.getEmail());
        String request = "{"
                + "\"name\": \"test1\","
                + "\"email\": \"test1@yandex.ru\""
                + "}";
        RequestUserDto result = json.parseObject(request);

        Assertions.assertEquals(result, requestUserDto);
    }
}
