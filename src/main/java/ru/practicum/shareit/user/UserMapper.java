package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserModifyDto;
import ru.practicum.shareit.user.model.User;

public class UserMapper {
    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail());
    }

    public static User toUser(UserModifyDto userModifyDto) {
        return User.builder()
                .name(userModifyDto.getName())
                .email(userModifyDto.getEmail())
                .build();
    }
}
