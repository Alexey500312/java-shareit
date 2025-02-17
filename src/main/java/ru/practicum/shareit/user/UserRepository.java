package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findUserById(Long userId);

    List<User> getUsers();

    User createUser(User user);

    User updateUser(User user);

    void deleteUser(User user);
}
