package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemModifyDto;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.isAvailable());
    }

    public static Item toItem(ItemModifyDto itemModifyDto) {
        return Item.builder()
                .name(itemModifyDto.getName())
                .description(itemModifyDto.getDescription())
                .available(itemModifyDto.getAvailable())
                .build();
    }
}
