package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.DataAlreadyExistException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.CommentRepository;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.dto.RequestUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final UserMapper mapper;

    @Override
    public UserDto getUser(Long userId) {
        return mapper.toUserDto(findUserById(userId));
    }

    @Override
    @Transactional
    public UserDto createUser(RequestUserDto requestUserDto) {
        checkUserEmail(requestUserDto.getEmail());
        User user = mapper.toUser(requestUserDto);
        return mapper.toUserDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserDto updateUser(Long userId, RequestUserDto requestUserDto) {
        User user = findUserById(userId);
        final String email = requestUserDto.getEmail();
        if (email != null && !email.equalsIgnoreCase(user.getEmail())) {
            checkUserEmail(email);
        }
        User newUser = new User(
                userId,
                requestUserDto.getName() != null ? requestUserDto.getName() : user.getName(),
                email != null ? email : user.getEmail());
        return mapper.toUserDto(userRepository.save(newUser));
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        User user = findUserById(userId);
        commentRepository.deleteByItemOwnerId(user.getId());
        bookingRepository.deleteByItemOwnerId(user.getId());
        itemRepository.deleteByOwnerId(user.getId());
        userRepository.delete(user);
    }

    private void checkUserEmail(final String email) {
        if (userRepository.findByEmailContainsIgnoreCase(email).isPresent()) {
            throw new DataAlreadyExistException(String.format("Email %s уже зарегестрирован!", email));
        }
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id = %d не найден", userId)));
    }
}
