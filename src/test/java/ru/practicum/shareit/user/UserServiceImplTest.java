package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemInMemoryRepository;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemModifyDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserModifyDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserServiceImplTest {
    private UserService userService;
    private ItemService itemService;

    static UserModifyDto getUserModifyDtoTest() {
        return new UserModifyDto("test", "test@yandex.ru");
    }

    static UserDto getUserDtoTest() {
        return new UserDto(1L, "test", "test@yandex.ru");
    }

    static ItemModifyDto getItemModifyDtoTest() {
        return new ItemModifyDto("test name", "test description", true);
    }

    static ItemDto getItemDtoTest() {
        return new ItemDto(1L, "test name", "test description", true);
    }

    @BeforeEach
    public void init() {
        UserRepository userRepository = new UserInMemoryRepository();
        ItemRepository itemRepository = new ItemInMemoryRepository();
        userService = new UserServiceImpl(userRepository, itemRepository);
        itemService = new ItemServiceImpl(itemRepository, userRepository);
    }

    @Test
    @DisplayName("Получить пользователя")
    public void shouldGetUser() {
        UserDto userDto = userService.createUser(getUserModifyDtoTest());
        Optional<UserDto> userDtoOptional = Optional.ofNullable(userService.getUser(userDto.getId()));

        assertThat(userDtoOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(getUserDtoTest());
    }

    @Test
    @DisplayName("Добавить пользователя")
    public void shouldCreateUser() {
        Optional<UserDto> userDtoOptional = Optional.ofNullable(userService.createUser(getUserModifyDtoTest()));

        assertThat(userDtoOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(getUserDtoTest());
    }

    @Test
    @DisplayName("Изменить пользователя")
    public void shouldUpdateUser() {
        UserDto userDto = userService.createUser(getUserModifyDtoTest());
        UserDto newUserDto = userDto.toBuilder().email("update@yandex.ru").build();
        Optional<UserDto> userDtoOptional = Optional.ofNullable(userService.updateUser(userDto.getId(),
                getUserModifyDtoTest().toBuilder()
                        .email("update@yandex.ru")
                        .build()));

        assertThat(userDtoOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(newUserDto);
    }

    @Test
    @DisplayName("Удалить пользователя")
    public void shouldDeleteUser() {
        UserDto userDto = userService.createUser(getUserModifyDtoTest());
        Optional<UserDto> userDtoOptional = Optional.ofNullable(userDto);

        assertThat(userDtoOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(getUserDtoTest());

        ItemDto itemDto = itemService.createItem(userDto.getId(), getItemModifyDtoTest());
        Optional<ItemDto> itemDtoOptional = Optional.ofNullable(itemDto);

        assertThat(itemDtoOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(getItemDtoTest());

        Long userId = userDto.getId();
        userService.deleteUser(userId);

        assertThrows(NotFoundException.class,
                () -> userService.getUser(userId));

        Optional<List<ItemDto>> itemsDtoOptional = Optional.of(itemService.getItemByUser(userId));

        assertThat(itemsDtoOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(new ArrayList<>());
    }
}
