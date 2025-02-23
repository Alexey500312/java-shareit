package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
public class UserInMemoryRepository implements UserRepository {
    private Long id = 0L;
    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> uniqueEmails = new HashSet<>();

    @Override
    public Optional<User> findUserById(Long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public Set<String> getRegisteredEmails() {
        return uniqueEmails;
    }

    @Override
    public User createUser(User user) {
        user.setId(getId());
        users.put(user.getId(), user);
        uniqueEmails.add(user.getEmail().toLowerCase());
        return user;
    }

    @Override
    public User updateUser(User user) {
        User userOld = users.put(user.getId(), user);
        final String newEmail = user.getEmail().toLowerCase();
        final String oldEmail = userOld.getEmail().toLowerCase();
        if (!newEmail.equals(oldEmail)) {
            uniqueEmails.remove(oldEmail);
            uniqueEmails.add(newEmail);
        }
        return user;
    }

    @Override
    public void deleteUser(User user) {
        uniqueEmails.remove(user.getEmail().toLowerCase());
        users.remove(user.getId());
    }

    private Long getId() {
        return ++id;
    }
}
