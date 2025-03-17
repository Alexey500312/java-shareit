package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.RequestUserDto;
import ru.practicum.shareit.user.dto.UserDto;

public interface UserService {
    UserDto getUser(Long userId);

    UserDto createUser(RequestUserDto requestUserDto);

    UserDto updateUser(Long userId, RequestUserDto requestUserDto);

    void deleteUser(Long userId);
}
