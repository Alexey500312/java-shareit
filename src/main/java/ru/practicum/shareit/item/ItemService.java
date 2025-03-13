package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.*;

import java.util.List;

public interface ItemService {
    ItemInfoDto getItem(Long userId, Long itemId);

    List<ItemInfoDto> getItemByUser(Long userId);

    List<ItemDto> searchItem(String name);

    ItemDto createItem(Long userId, RequestItemDto requestItemDto);

    ItemDto updateItem(Long userId, Long itemId, RequestItemDto requestItemDto);

    void deleteItem(Long userId, Long itemId);

    CommentDto createComment(Long userId, Long itemId, RequestCommentDto requestCommentDto);
}
