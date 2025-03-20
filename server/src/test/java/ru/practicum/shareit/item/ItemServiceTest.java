package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.TestData;
import ru.practicum.shareit.exception.CRUDException;
import ru.practicum.shareit.exception.ChekParamException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
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
public class ItemServiceTest {
    private final ItemService itemService;
    private final ItemRequestService itemRequestService;
    private final ItemMapper itemMapper;
    private final UserMapper userMapper;

    @Test
    @DisplayName("Получить вещь")
    public void shouldGetItem() {
        User user = TestData.getUser();
        ItemInfoDto itemInfoDtoTest = TestData.getItemInfoDto();
        ItemInfoDto itemInfoDto = itemService.getItem(user.getId(), itemInfoDtoTest.getId());

        assertEquals(itemInfoDtoTest, itemInfoDto);

        itemInfoDtoTest = itemInfoDto.toBuilder()
                .lastBooking(null)
                .nextBooking(null)
                .build();
        itemInfoDto = itemService.getItem(TestData.getUser2().getId(), itemInfoDtoTest.getId());

        assertEquals(itemInfoDtoTest, itemInfoDto);

        assertThrows(NotFoundException.class,
                () -> itemService.getItem(user.getId(), -1L));
    }

    @Test
    @DisplayName("Поиск вещи по владельцу")
    public void shouldGetItemByUser() {
        User user = TestData.getUser();
        List<ItemInfoDto> itemInfoDtoTest = TestData.getItemsInfoDtoByOwner();
        Optional<List<ItemInfoDto>> itemInfoDtoOptional = Optional.of(itemService.getItemByUser(user.getId()));

        assertThat(itemInfoDtoOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(itemInfoDtoTest);
    }

    @Test
    @DisplayName("Поиск вещи по наименованию или описанию")
    public void shouldSearchItem() {
        List<ItemDto> itemDtoTest = List.of(itemMapper.toItemDto(TestData.getItem()));
        Optional<List<ItemDto>> itemDtoOptional = Optional.of(itemService.searchItem("DeScR"));

        assertThat(itemDtoOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(itemDtoTest);

        itemDtoOptional = Optional.of(itemService.searchItem(null));

        assertThat(itemDtoOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(List.of());

        itemDtoOptional = Optional.of(itemService.searchItem("   "));

        assertThat(itemDtoOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(List.of());
    }

    @Test
    @DisplayName("Создать вещь")
    public void shouldCreateItem() {
        User user = TestData.getUser();
        RequestItemRequestDto requestItemRequestDto = new RequestItemRequestDto(
                "new description");
        ItemRequestDto itemRequestDto = itemRequestService.createItemRequest(
                TestData.getUser2().getId(), requestItemRequestDto);
        RequestItemDto requestItemDto = new RequestItemDto(
                "new item",
                "new description",
                true,
                itemRequestDto.getId());

        ItemDto itemDto = itemService.createItem(user.getId(), requestItemDto);

        assertEquals(itemDto.getName(), requestItemDto.getName());
        assertEquals(itemDto.getDescription(), requestItemDto.getDescription());
        assertEquals(itemDto.getAvailable(), requestItemDto.getAvailable());
        assertEquals(itemDto.getOwner(), userMapper.toUserDto(user));
        assertEquals(itemDto.getAvailable(), requestItemDto.getAvailable());

        assertThrows(NotFoundException.class,
                () -> itemService.createItem(-1L, requestItemDto));

        assertThrows(NotFoundException.class,
                () -> itemService.createItem(user.getId(), requestItemDto.toBuilder()
                        .requestId(-1L)
                        .build()));
    }

    @Test
    @DisplayName("Изменить вещь")
    public void shouldUpdateItem() {
        User user = TestData.getUser();
        ItemDto itemDtoTest = itemMapper.toItemDto(TestData.getItem()).toBuilder()
                .name("update name")
                .build();
        ItemDto itemDto = itemService.updateItem(
                user.getId(),
                itemDtoTest.getId(),
                RequestItemDto.builder()
                        .name("update name")
                        .build());

        assertEquals(itemDtoTest, itemDto);

        itemDtoTest = itemDtoTest.toBuilder()
                .description("update description")
                .build();
        itemDto = itemService.updateItem(
                user.getId(),
                itemDtoTest.getId(),
                RequestItemDto.builder()
                        .description("update description")
                        .build());

        assertEquals(itemDtoTest, itemDto);

        itemDtoTest = itemDtoTest.toBuilder()
                .available(false)
                .build();
        itemDto = itemService.updateItem(
                user.getId(),
                itemDtoTest.getId(),
                RequestItemDto.builder()
                        .available(false)
                        .build());

        assertEquals(itemDtoTest, itemDto);

        final Long userId = user.getId();
        final Long itemId = itemDtoTest.getId();
        RequestItemDto requestItemDto = new RequestItemDto(
                "update name",
                "update description",
                false,
                TestData.getItemRequest().getId());

        assertThrows(NotFoundException.class,
                () -> itemService.updateItem(-1L, itemId, requestItemDto));
        assertThrows(NotFoundException.class,
                () -> itemService.updateItem(userId, -1L, requestItemDto));
        assertThrows(CRUDException.class,
                () -> itemService.updateItem(TestData.getUser2().getId(), itemId, requestItemDto));
    }

    @Test
    @DisplayName("Удалить вещь")
    public void shouldDeleteItem() {
        User user = TestData.getUser();
        ItemInfoDto itemInfoDtoTest = TestData.getItemInfoDto();
        ItemInfoDto itemInfoDto = itemService.getItem(user.getId(), itemInfoDtoTest.getId());

        assertEquals(itemInfoDtoTest, itemInfoDto);
        assertThrows(NotFoundException.class,
                () -> itemService.deleteItem(user.getId(), -1L));
        assertThrows(NotFoundException.class,
                () -> itemService.deleteItem(-1L, itemInfoDtoTest.getId()));
        assertThrows(CRUDException.class,
                () -> itemService.deleteItem(TestData.getUser2().getId(), itemInfoDtoTest.getId()));

        itemService.deleteItem(user.getId(), itemInfoDtoTest.getId());

        assertThrows(NotFoundException.class,
                () -> itemService.getItem(user.getId(), itemInfoDtoTest.getId()));
    }

    @Test
    @DisplayName("Создать комментарий")
    public void shouldCreateComment() {
        User user = TestData.getUser2();
        Item item = TestData.getItem();
        RequestCommentDto requestCommentDto = new RequestCommentDto("new comment");
        CommentDto commentDto = itemService.createComment(user.getId(), item.getId(), requestCommentDto);

        assertEquals(user.getName(), commentDto.getAuthorName());
        assertEquals(requestCommentDto.getText(), commentDto.getText());
        assertThrows(NotFoundException.class,
                () -> itemService.createComment(-1L, item.getId(), requestCommentDto));
        assertThrows(NotFoundException.class,
                () -> itemService.createComment(user.getId(), -1L, requestCommentDto));
        assertThrows(ChekParamException.class,
                () -> itemService.createComment(user.getId(), TestData.getItem2().getId(), requestCommentDto));
    }
}
