package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemModifyDto;
import ru.practicum.shareit.user.UserInMemoryRepository;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemServiceImplTest {
    private ItemService itemService;
    private UserRepository userRepository;

    static User getUserTest() {
        return new User(1L, "test name", "test@yandex.ru");
    }

    static ItemModifyDto getItemModifyDtoTest() {
        return new ItemModifyDto("test name", "test description", true);
    }

    static ItemDto getItemDtoTest() {
        return new ItemDto(1L, "test name", "test description", true);
    }

    @BeforeEach
    public void init() {
        userRepository = new UserInMemoryRepository();
        itemService = new ItemServiceImpl(new ItemInMemoryRepository(), userRepository);
    }

    @Test
    @DisplayName("Получить вещь")
    public void shouldGetItem() {
        User user = getUserTest();
        userRepository.createUser(user);
        ItemModifyDto itemModifyDto = getItemModifyDtoTest();
        itemService.createItem(user.getId(), itemModifyDto);
        ItemDto itemDto = getItemDtoTest();

        Optional<ItemDto> itemDtoOptional = Optional.ofNullable(itemService.getItem(itemDto.getId()));

        assertThat(itemDtoOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(itemDto);
    }

    @Test
    @DisplayName("Получить все вещи пользователя")
    public void shouldGetItemByUser() {
        User user = getUserTest();
        userRepository.createUser(user);
        ItemModifyDto itemModifyDto = getItemModifyDtoTest();
        itemService.createItem(user.getId(), itemModifyDto);
        user = User.builder()
                .name("test2")
                .email("test2@yandex.ru")
                .build();
        userRepository.createUser(user);
        itemModifyDto = ItemModifyDto.builder()
                .name("test2")
                .description("test2 description")
                .available(true)
                .build();
        itemService.createItem(user.getId(), itemModifyDto);
        List<ItemDto> itemDtos = new ArrayList<>();
        itemDtos.add(itemService.getItem(2L));
        Optional<List<ItemDto>> itemDtoOptional = Optional.of(itemService.getItemByUser(user.getId()));

        assertThat(itemDtoOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(itemDtos);
    }

    @Test
    @DisplayName("Получить все вещи пользователя")
    public void shouldSearchUser() {
        User user = getUserTest();
        userRepository.createUser(user);
        ItemModifyDto itemModifyDto = getItemModifyDtoTest();
        itemService.createItem(user.getId(), itemModifyDto);
        itemModifyDto = ItemModifyDto.builder()
                .name("test2")
                .description("test2 description")
                .available(true)
                .build();
        itemService.createItem(user.getId(), itemModifyDto);
        List<ItemDto> itemDtos = new ArrayList<>();
        itemDtos.add(itemService.getItem(2L));
        Optional<List<ItemDto>> itemDtoOptional = Optional.of(itemService.searchItem("test2"));

        assertThat(itemDtoOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(itemDtos);
    }

    @Test
    @DisplayName("Добавить вещь")
    public void shouldCreateItem() {
        User user = getUserTest();
        userRepository.createUser(user);
        Optional<ItemDto> itemDtoOptional = Optional.ofNullable(itemService.createItem(user.getId(), getItemModifyDtoTest()));

        assertThat(itemDtoOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(getItemDtoTest());
    }

    @Test
    @DisplayName("Изменить вещь")
    public void shouldUpdateItem() {
        User user = getUserTest();
        userRepository.createUser(user);
        ItemModifyDto itemModifyDto = getItemModifyDtoTest();
        ItemDto itemDto = itemService.createItem(user.getId(), itemModifyDto);
        ItemDto newItemDto = itemDto.toBuilder().name("update test").build();
        Optional<ItemDto> itemDtoOptional = Optional.ofNullable(itemService.updateItem(user.getId(), itemDto.getId(),
                getItemModifyDtoTest().toBuilder()
                        .name("update test")
                        .build()));

        assertThat(itemDtoOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(newItemDto);
    }

    @Test
    @DisplayName("Удалить вещь")
    public void shouldDeleteItem() {
        User user = getUserTest();
        userRepository.createUser(user);
        ItemDto itemDto = itemService.createItem(user.getId(), getItemModifyDtoTest());
        Optional<ItemDto> itemDtoOptional = Optional.ofNullable(itemDto);

        assertThat(itemDtoOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(getItemDtoTest());

        itemService.deleteItem(user.getId(), itemDto.getId());
        Optional<List<ItemDto>> itemsDtoOptional = Optional.of(itemService.getItemByUser(user.getId()));

        assertThat(itemsDtoOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(new ArrayList<>());
    }
}
