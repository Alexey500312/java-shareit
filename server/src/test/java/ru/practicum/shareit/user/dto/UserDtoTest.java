package ru.practicum.shareit.user.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.context.annotation.Import;
import ru.practicum.shareit.TestData;
import ru.practicum.shareit.user.UserMapper;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@Import({UserMapper.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDtoTest {
    private final JacksonTester<UserDto> json;
    private final UserMapper userMapper;

    @Test
    @DisplayName("Сериализация")
    public void shouldSerialization() throws Exception {
        UserDto userDto = userMapper.toUserDto(TestData.getUser());
        JsonContent<UserDto> result = json.write(userDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(userDto.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(userDto.getName());
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo(userDto.getEmail());
    }

    @Test
    @DisplayName("Десериализация")
    public void shouldDeserialization() throws Exception {
        UserDto userDto = userMapper.toUserDto(TestData.getUser());
        String request = "{"
                + "\"id\": 1,"
                + "\"name\": \"test1\","
                + "\"email\": \"test1@yandex.ru\""
                + "}";
        UserDto result = json.parseObject(request);

        assertThat(result.getId()).isEqualTo(userDto.getId());
        assertThat(result.getName()).isEqualTo(userDto.getName());
        assertThat(result.getEmail()).isEqualTo(userDto.getEmail());
    }
}
