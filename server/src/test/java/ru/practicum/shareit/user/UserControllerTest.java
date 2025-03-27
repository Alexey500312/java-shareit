package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.TestData;
import ru.practicum.shareit.exception.DataAlreadyExistException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.RequestUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
@Import({UserMapper.class})
public class UserControllerTest {
    @MockBean
    private UserService userService;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ObjectMapper mapper;

    @Test
    @DisplayName("Получить пользователя по id")
    public void shouldGetUser() throws Exception {
        UserDto userDto = userMapper.toUserDto(TestData.getUser());

        when(userService.getUser(userDto.getId())).thenReturn(userDto);

        mvc.perform(get("/users/" + userDto.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(userDto)));

        User wrongUser = TestData.getWrongUser();

        when(userService.getUser(wrongUser.getId())).thenThrow(NotFoundException.class);

        mvc.perform(get("/users/" + wrongUser.getId()))
                .andExpect(status().is4xxClientError());

        verify(userService, times(1)).getUser(userDto.getId());
        verify(userService, times(1)).getUser(wrongUser.getId());
    }

    @Test
    @DisplayName("Добавление пользователя")
    public void shouldCreateUser() throws Exception {
        User user = new User(3L, "new user", "newuser@yandex.ru");
        RequestUserDto requestUserDto = new RequestUserDto(user.getName(), user.getEmail());
        UserDto userDto = userMapper.toUserDto(user);

        when(userService.createUser(requestUserDto)).thenReturn(userDto);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(requestUserDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(mapper.writeValueAsString(userDto)));

        when(userService.createUser(requestUserDto)).thenThrow(DataAlreadyExistException.class);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(requestUserDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());

        verify(userService, times(2)).createUser(requestUserDto);
    }

    @Test
    @DisplayName("Изменение пользователя")
    public void shouldUpdateUser() throws Exception {
        User user = TestData.getUser().toBuilder()
                .name("update name")
                .build();
        RequestUserDto requestUserDto = new RequestUserDto(user.getName(), null);
        UserDto userDto = userMapper.toUserDto(user);

        when(userService.updateUser(user.getId(), requestUserDto)).thenReturn(userDto);

        mvc.perform(patch("/users/" + user.getId())
                        .content(mapper.writeValueAsString(requestUserDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(mapper.writeValueAsString(userDto)));

        User updateDoubleEmail = user.toBuilder()
                .email("test2@yandex.ru")
                .build();
        RequestUserDto requestUpdateDoubleEmailDto = new RequestUserDto(null, updateDoubleEmail.getEmail());

        when(userService.updateUser(updateDoubleEmail.getId(), requestUpdateDoubleEmailDto))
                .thenThrow(DataAlreadyExistException.class);

        mvc.perform(patch("/users/" + user.getId())
                        .content(mapper.writeValueAsString(requestUpdateDoubleEmailDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());

        verify(userService, times(1)).updateUser(user.getId(), requestUserDto);
        verify(userService, times(1)).updateUser(user.getId(), requestUpdateDoubleEmailDto);
    }

    @Test
    @DisplayName("Удаление пользователя")
    public void shouldDeleteUser() throws Exception {
        User user = TestData.getUser();

        mvc.perform(delete("/users/" + user.getId()))
                .andExpect(status().is2xxSuccessful());

        User wrongUser = TestData.getWrongUser();

        mvc.perform(delete("/users/" + wrongUser.getId()))
                .andExpect(status().is2xxSuccessful());

        verify(userService, times(1)).deleteUser(user.getId());
        verify(userService, times(1)).deleteUser(wrongUser.getId());
    }
}
