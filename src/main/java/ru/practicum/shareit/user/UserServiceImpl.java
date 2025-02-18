package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DataAlreadyExistException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserModifyDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public UserDto getUser(Long userId) {
        return UserMapper.toUserDto(findUserById(userId));
    }

    @Override
    public UserDto createUser(UserModifyDto userModifyDto) {
        checkUserEmail(userModifyDto.getEmail());
        User user = UserMapper.toUser(userModifyDto);
        return UserMapper.toUserDto(userRepository.createUser(user));
    }

    @Override
    public UserDto updateUser(Long userId, UserModifyDto userModifyDto) {
        User user = findUserById(userId);
        final String email = userModifyDto.getEmail();
        if (email != null && !email.equalsIgnoreCase(user.getEmail())) {
            checkUserEmail(email);
        }
        User newUser = new User(
                userId,
                userModifyDto.getName() != null ? userModifyDto.getName() : user.getName(),
                email != null ? email : user.getEmail());
        return UserMapper.toUserDto(userRepository.updateUser(newUser));
    }

    @Override
    public void deleteUser(Long userId) {
        User user = findUserById(userId);
        List<Item> items = itemRepository.getItemByUser(user.getId()).stream().toList();
        for (Item item : items) {
            itemRepository.deleteItem(item);
        }
        userRepository.deleteUser(user);
    }

    private void checkUserEmail(final String email) {
        if (userRepository.getRegisteredEmails().contains(email.toLowerCase())) {
            throw new DataAlreadyExistException(String.format("Email %s уже зарегестрирован!", email));
        }
    }

    private User findUserById(Long userId) {
        return userRepository.findUserById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id = %d не найден", userId)));
    }
}
