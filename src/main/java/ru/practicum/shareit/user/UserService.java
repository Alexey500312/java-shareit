package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserModifyDto;

public interface UserService {
    UserDto getUser(Long userId);

    UserDto createUser(UserModifyDto userModifyDto);

    UserDto updateUser(Long userId, UserModifyDto userModifyDto);

    void deleteUser(Long userId);
}
