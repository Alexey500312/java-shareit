package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemByRequestDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.dto.RequestItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;

@Component
@RequiredArgsConstructor
public class ItemMapper {
    private final UserMapper userMapper;

    public ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                userMapper.toUserDto(item.getOwner()),
                item.isAvailable());
    }

    public ItemInfoDto toItemInfoDto(Item item) {
        return ItemInfoDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.isAvailable())
                .build();
    }

    public ItemByRequestDto toItemByRequestDto(Item item) {
        return ItemByRequestDto.builder()
                .id(item.getId())
                .name(item.getName())
                .ownerId(item.getOwner().getId())
                .build();
    }

    public Item toItem(RequestItemDto requestItemDto) {
        return Item.builder()
                .name(requestItemDto.getName())
                .description(requestItemDto.getDescription())
                .available(requestItemDto.getAvailable())
                .build();
    }
}
