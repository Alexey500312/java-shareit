package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.ShortBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.CRUDException;
import ru.practicum.shareit.exception.ChekParamException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final ItemRequestRepository itemRequestorRepository;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;
    private final BookingMapper bookingMapper;

    @Override
    public ItemInfoDto getItem(Long userId, Long itemId) {
        Item item = findItemById(itemId);
        ItemInfoDto itemInfoDto = itemMapper.toItemInfoDto(item);
        Map<Long, ShortBookingDto> lastBookings = new HashMap<>();
        Map<Long, ShortBookingDto> nextBookings = new HashMap<>();
        if (item.getOwner().getId().equals(userId)) {
            lastBookings = getLastNextBookings(userId, List.of(itemInfoDto.getId()), BookingType.LAST);
            nextBookings = getLastNextBookings(userId, List.of(itemInfoDto.getId()), BookingType.NEXT);
        }
        List<CommentDto> comments = commentRepository.findByItemId(itemInfoDto.getId()).stream()
                .map(commentMapper::toCommentDto)
                .toList();
        return itemInfoDto.toBuilder()
                .lastBooking(lastBookings.get(itemInfoDto.getId()))
                .nextBooking(nextBookings.get(itemInfoDto.getId()))
                .comments(comments)
                .build();
    }

    @Override
    public List<ItemInfoDto> getItemByUser(Long userId) {
        List<Item> items = itemRepository.findByOwnerId(userId);
        List<Long> itemIds = items.stream()
                .map(Item::getId)
                .toList();
        Map<Long, ShortBookingDto> lastBookings = getLastNextBookings(userId, itemIds, BookingType.LAST);
        Map<Long, ShortBookingDto> nextBookings = getLastNextBookings(userId, itemIds, BookingType.NEXT);
        Map<Long, List<CommentDto>> comments = commentRepository.findByItemIdIn(itemIds).stream()
                .collect(Collectors.groupingBy(c -> c.getItem().getId(),
                        Collectors.mapping(commentMapper::toCommentDto, Collectors.toList())));
        return items.stream()
                .map(itemMapper::toItemInfoDto)
                .peek(i -> {
                    i.setLastBooking(lastBookings.get(i.getId()));
                    i.setNextBooking(nextBookings.get(i.getId()));
                    i.setComments(comments.get(i.getId()));
                })
                .toList();
    }

    @Override
    public List<ItemDto> searchItem(String name) {
        return name != null && !name.isBlank()
                ? itemRepository.searchItem(name).stream()
                .map(itemMapper::toItemDto)
                .toList()
                : new ArrayList<>();
    }

    @Override
    @Transactional
    public ItemDto createItem(Long userId, RequestItemDto requestItemDto) {
        User user = findUserById(userId);
        ItemRequest itemRequest = null;
        if (requestItemDto.getRequestId() != null) {
            itemRequest = itemRequestorRepository.findById(requestItemDto.getRequestId())
                    .orElseThrow(() -> new NotFoundException(
                            String.format("Запрос с ид = %d не найден", requestItemDto.getRequestId())));
        }
        Item item = itemMapper.toItem(requestItemDto);
        item.setOwner(user);
        item.setRequest(itemRequest);
        return itemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    @Transactional
    public ItemDto updateItem(Long userId, Long itemId, RequestItemDto requestItemDto) {
        User user = findUserById(userId);
        Item item = findItemById(itemId);
        if (!item.getOwner().getId().equals(user.getId())) {
            throw new CRUDException("Изменение доступно только для своих вещей");
        }
        Item newItem = new Item(
                itemId,
                requestItemDto.getName() != null ? requestItemDto.getName() : item.getName(),
                requestItemDto.getDescription() != null ? requestItemDto.getDescription() : item.getDescription(),
                requestItemDto.getAvailable() != null ? requestItemDto.getAvailable() : item.isAvailable(),
                item.getOwner(),
                item.getRequest());
        return itemMapper.toItemDto(itemRepository.save(newItem));
    }

    @Override
    @Transactional
    public void deleteItem(Long userId, Long itemId) {
        User user = findUserById(userId);
        Item item = findItemById(itemId);
        if (!item.getOwner().getId().equals(user.getId())) {
            throw new CRUDException("Удаление доступно только для своих вещей");
        }
        itemRepository.delete(item);
    }

    @Override
    @Transactional
    public CommentDto createComment(Long userId, Long itemId, RequestCommentDto requestCommentDto) {
        User user = findUserById(userId);
        Item item = findItemById(itemId);
        bookingRepository.findPastBooking(user.getId(), item.getId())
                .orElseThrow(() -> new ChekParamException(
                        String.format("У пользователя с id = %d не найдено завершенное бронирование вещи с id = %d",
                                user.getId(),
                                item.getId())));
        Comment comment = Comment.builder()
                .text(requestCommentDto.getText())
                .item(item)
                .author(user)
                .created(LocalDateTime.now())
                .build();
        return commentMapper.toCommentDto(commentRepository.save(comment));
    }

    private Item findItemById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Вещь с id = %d не найден", itemId)));
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id = %d не найден", userId)));
    }

    private Map<Long, ShortBookingDto> getLastNextBookings(Long userId, List<Long> itemIds, BookingType type) {
        List<Booking> bookings = switch (type) {
            case LAST -> bookingRepository.findLastBooking(userId, itemIds);
            case NEXT -> bookingRepository.findNextBooking(userId, itemIds);
        };
        return bookings.stream()
                .collect(Collectors.toMap(b -> b.getItem().getId(), bookingMapper::toShortBookingDto));
    }

    private enum BookingType {
        LAST,
        NEXT
    }
}
