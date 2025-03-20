package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.dto.RequestItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestWithItemsDto getItemById(Long requestId);

    List<ItemRequestWithItemsDto> getItemRequestsByUser(Long userId);

    List<ItemRequestDto> getItemRequestsAll(Long userId);

    ItemRequestDto createItemRequest(Long userId, RequestItemRequestDto requestItemRequestDto);
}
