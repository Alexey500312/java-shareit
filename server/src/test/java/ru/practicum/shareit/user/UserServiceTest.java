package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.TestData;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.exception.DataAlreadyExistException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.RequestItemDto;
import ru.practicum.shareit.user.dto.RequestUserDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceTest {
    private final UserService userService;
    private final UserMapper userMapper;
    private final ItemService itemService;
    private final BookingService bookingService;

    @Test
    @DisplayName("Получить пользователя по id")
    public void shouldGetUser() {
        UserDto userDto = userMapper.toUserDto(TestData.getUser());
        UserDto findUser = userService.getUser(userDto.getId());

        assertEquals(userDto, findUser);

        assertThrows(NotFoundException.class,
                () -> userService.getUser(TestData.getWrongUser().getId()));
    }

    @Test
    @DisplayName("Добавление пользователя")
    public void shouldCreateUser() {
        RequestUserDto requestUserDto = new RequestUserDto("new user", "newuser@yandex.ru");
        UserDto responseUserDto = userService.createUser(requestUserDto);

        assertEquals(requestUserDto.getName(), responseUserDto.getName());
        assertEquals(requestUserDto.getEmail(), responseUserDto.getEmail());

        UserDto findUser = userService.getUser(responseUserDto.getId());

        assertEquals(responseUserDto, findUser);

        assertThrows(DataAlreadyExistException.class,
                () -> userService.createUser(requestUserDto));
    }

    @Test
    @DisplayName("Изменение пользователя")
    public void shouldUpdateUser() {
        RequestUserDto requestUserDto = new RequestUserDto("new user", "newuser@yandex.ru");
        UserDto responseUserDto = userService.createUser(requestUserDto);

        final Long userId = responseUserDto.getId();
        RequestUserDto newRequestUserDto = requestUserDto.toBuilder()
                .name(null)
                .email("newuser@yandex.ru")
                .build();
        responseUserDto = userService.updateUser(userId, newRequestUserDto);

        assertEquals(requestUserDto.getName(), responseUserDto.getName());
        assertEquals(newRequestUserDto.getEmail(), responseUserDto.getEmail());

        newRequestUserDto = requestUserDto.toBuilder()
                .name("new user update")
                .email(null)
                .build();
        responseUserDto = userService.updateUser(userId, newRequestUserDto);

        assertEquals(newRequestUserDto.getName(), responseUserDto.getName());
        assertEquals(requestUserDto.getEmail(), responseUserDto.getEmail());

        assertEquals(responseUserDto, userService.getUser(responseUserDto.getId()));

        assertThrows(DataAlreadyExistException.class,
                () -> userService.updateUser(userId, RequestUserDto.builder()
                        .email(TestData.getUser().getEmail())
                        .build()));
    }

    @Test
    @DisplayName("Удаление пользователя")
    public void shouldDeleteUser() {
        RequestUserDto requestUserDto = new RequestUserDto("new user", "newuser@yandex.ru");
        RequestItemDto requestItemDto = new RequestItemDto("new item", "new description", true, null);
        UserDto responseUserDto = userService.createUser(requestUserDto);
        ItemDto responseItemDto = itemService.createItem(responseUserDto.getId(), requestItemDto);
        RequestBookingDto requestBookingDto = new RequestBookingDto(
                null,
                responseItemDto.getId(),
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                responseUserDto.getId());
        bookingService.createBooking(responseUserDto.getId(), requestBookingDto);

        assertEquals(responseUserDto, userService.getUser(responseUserDto.getId()));
        assertEquals(1, itemService.getItemByUser(responseUserDto.getId()).size());
        assertEquals(1, bookingService.getBookingsByUser(responseUserDto.getId(), BookingState.ALL.name()).size());

        userService.deleteUser(responseUserDto.getId());

        assertThrows(NotFoundException.class,
                () -> userService.getUser(responseUserDto.getId()));
        assertEquals(0, itemService.getItemByUser(responseUserDto.getId()).size());
        assertEquals(0, bookingService.getBookingsByUser(responseUserDto.getId(), BookingState.ALL.name()).size());
    }
}
