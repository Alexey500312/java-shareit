package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.TestData;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.dto.RequestItemRequestDto;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestServiceTest {
    private final ItemRequestService itemRequestService;
    private final ItemRequestMapper itemRequestMapper;
    private final UserMapper userMapper;

    @Test
    @DisplayName("Получить запрос")
    public void shouldGetItemById() {
        ItemRequestWithItemsDto itemRequestWithItemsDtoTest = TestData.getItemRequestWithItemsDto();
        ItemRequestWithItemsDto itemRequestWithItemsDto = itemRequestService.getItemById(itemRequestWithItemsDtoTest.getId());

        assertEquals(itemRequestWithItemsDtoTest, itemRequestWithItemsDto);
        assertThrows(NotFoundException.class,
                () -> itemRequestService.getItemById(-1L));
    }

    @Test
    @DisplayName("Список всех запросов созданных пользователем с сортировкой")
    public void shouldGetItemRequestsByUser() {
        User user = TestData.getUser();
        List<ItemRequestWithItemsDto> itemRequestWithItemsDtoTest = TestData.getItemRequestsByUser();
        Optional<List<ItemRequestWithItemsDto>> itemRequestWithItemsDtoOptional = Optional.of(
                itemRequestService.getItemRequestsByUser(user.getId()));

        assertThat(itemRequestWithItemsDtoOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(itemRequestWithItemsDtoTest);

        user = TestData.getUser2();
        itemRequestWithItemsDtoTest = List.of(TestData.getItemRequestWithItemsDto());
        itemRequestWithItemsDtoOptional = Optional.of(itemRequestService.getItemRequestsByUser(user.getId()));

        assertThat(itemRequestWithItemsDtoOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(itemRequestWithItemsDtoTest);
    }

    @Test
    @DisplayName("Список всех запросов кроме созданных пользователем с сортировкой")
    public void shouldGetItemRequestsAll() {
        User user = TestData.getUser();
        List<ItemRequestDto> itemRequestWithItemsDtoTest = List.of(
                itemRequestMapper.toItemRequestDto(TestData.getItemRequest()));
        Optional<List<ItemRequestDto>> itemRequestWithItemsDtoOptional = Optional.of(
                itemRequestService.getItemRequestsAll(user.getId()));

        assertThat(itemRequestWithItemsDtoOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(itemRequestWithItemsDtoTest);

        user = TestData.getUser2();
        itemRequestWithItemsDtoTest = TestData.getFindByRequestorIdOrderByCreatedDesc().stream()
                .map(itemRequestMapper::toItemRequestDto)
                .toList();
        itemRequestWithItemsDtoOptional = Optional.of(itemRequestService.getItemRequestsAll(user.getId()));

        assertThat(itemRequestWithItemsDtoOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(itemRequestWithItemsDtoTest);
    }

    @Test
    @DisplayName("Создать запрос")
    public void shouldCreateItemRequest() {
        User user = TestData.getUser();
        RequestItemRequestDto requestItemRequestDto = new RequestItemRequestDto("new description");
        ItemRequestDto itemRequestDto = itemRequestService.createItemRequest(user.getId(), requestItemRequestDto);

        assertEquals(requestItemRequestDto.getDescription(), itemRequestDto.getDescription());
        assertEquals(userMapper.toUserDto(user), itemRequestDto.getRequestor());

        User wronfUser = TestData.getWrongUser();

        assertThrows(NotFoundException.class,
                () -> itemRequestService.createItemRequest(wronfUser.getId(), requestItemRequestDto));
    }
}
