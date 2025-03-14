package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.dto.ShortBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

@Component
@RequiredArgsConstructor
public class BookingMapper {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ItemMapper itemMapper;

    public Booking toBooking(RequestBookingDto requestBookingDto) {
        User user = userRepository.findById(requestBookingDto.getBookerId())
                .orElseThrow(() -> new NotFoundException(
                        String.format("Пользователь с id = %d не найден", requestBookingDto.getBookerId())));
        Item item = itemRepository.findById(requestBookingDto.getItemId())
                .orElseThrow(() -> new NotFoundException(
                        String.format("Вещь с id = %d не найдена", requestBookingDto.getItemId())));
        return Booking.builder()
                .id(requestBookingDto.getId())
                .start(requestBookingDto.getStart())
                .end(requestBookingDto.getEnd())
                .item(item)
                .booker(user)
                .build();
    }

    public BookingDto toBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(itemMapper.toItemDto(booking.getItem()))
                .booker(userMapper.toUserDto(booking.getBooker()))
                .status(booking.getStatus())
                .build();
    }

    public ShortBookingDto toShortBookingDto(Booking booking) {
        return ShortBookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .bookerName(booking.getBooker().getName())
                .build();
    }
}
