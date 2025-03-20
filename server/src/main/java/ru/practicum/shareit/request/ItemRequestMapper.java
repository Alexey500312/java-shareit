package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserMapper;

@Component
@RequiredArgsConstructor
public class ItemRequestMapper {
    private final UserMapper userMapper;

    public ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return new ItemRequestDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                userMapper.toUserDto(itemRequest.getRequestor()),
                itemRequest.getCreated());
    }

    public ItemRequestWithItemsDto toItemRequestWithItemsDto(ItemRequest itemRequest) {
        return ItemRequestWithItemsDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .build();
    }
}
