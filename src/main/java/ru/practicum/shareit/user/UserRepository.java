package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.Optional;
import java.util.Set;

public interface UserRepository {
    Optional<User> findUserById(Long userId);

    Set<String> getRegisteredEmails();

    User createUser(User user);

    User updateUser(User user);

    void deleteUser(User user);
}
