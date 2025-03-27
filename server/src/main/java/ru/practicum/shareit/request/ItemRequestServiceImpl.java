package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemByRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.dto.RequestItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemRequestMapper itemRequestMapper;
    private final ItemMapper itemMapper;

    @Override
    public ItemRequestWithItemsDto getItemById(Long requestId) {
        ItemRequest itemRequest = findItemRequestById(requestId);
        ItemRequestWithItemsDto itemRequestWithItemsDto = itemRequestMapper.toItemRequestWithItemsDto(itemRequest);
        Map<Long, List<ItemByRequestDto>> items = itemRepository.findByRequestIdIn(List.of(requestId)).stream()
                .collect(Collectors.groupingBy(c -> c.getRequest().getId(),
                        Collectors.mapping(itemMapper::toItemByRequestDto, Collectors.toList())));
        itemRequestWithItemsDto.setItems(items.get(itemRequestWithItemsDto.getId()));
        return itemRequestWithItemsDto;
    }

    @Override
    public List<ItemRequestWithItemsDto> getItemRequestsByUser(Long userId) {
        List<ItemRequest> itemRequests = itemRequestRepository.findByRequestorIdOrderByCreatedDesc(userId);
        List<Long> requestIds = itemRequests.stream()
                .map(ItemRequest::getId)
                .toList();
        Map<Long, List<ItemByRequestDto>> items = itemRepository.findByRequestIdIn(requestIds).stream()
                .collect(Collectors.groupingBy(c -> c.getRequest().getId(),
                        Collectors.mapping(itemMapper::toItemByRequestDto, Collectors.toList())));
        return itemRequests.stream()
                .map(itemRequestMapper::toItemRequestWithItemsDto)
                .peek(ir -> ir.setItems(items.get(ir.getId())))
                .toList();
    }

    @Override
    public List<ItemRequestDto> getItemRequestsAll(Long userId) {
        return itemRequestRepository.findByRequestorIdNotOrderByCreatedDesc(userId).stream()
                .map(itemRequestMapper::toItemRequestDto)
                .toList();
    }

    @Override
    @Transactional
    public ItemRequestDto createItemRequest(Long userId, RequestItemRequestDto requestItemRequestDto) {
        User user = findUserById(userId);
        ItemRequest itemRequest = ItemRequest.builder()
                .description(requestItemRequestDto.getDescription())
                .requestor(user)
                .created(LocalDateTime.now())
                .build();
        return itemRequestMapper.toItemRequestDto(itemRequestRepository.save(itemRequest));
    }

    private ItemRequest findItemRequestById(Long requestId) {
        return itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(String.format("Запрос с id = %d не найден", requestId)));
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id = %d не найден", userId)));
    }
}
