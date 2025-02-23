package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemModifyDto;

import java.util.List;

public interface ItemService {
    ItemDto getItem(Long itemId);

    List<ItemDto> getItemByUser(Long userId);

    List<ItemDto> searchItem(String name);

    ItemDto createItem(Long userId, ItemModifyDto itemModifyDto);

    ItemDto updateItem(Long userId, Long itemId, ItemModifyDto itemModifyDto);

    void deleteItem(Long userId, Long itemId);
}
