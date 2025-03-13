package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.ChekParamException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.UserRepository;

import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final BookingMapper bookingMapper;

    @Override
    public BookingDto getBooking(Long userId, Long bookingId) {
        Booking booking = findBookingById(bookingId);
        if (!booking.getBooker().getId().equals(userId) && !booking.getItem().getOwner().getId().equals(userId)) {
            throw new ChekParamException("Получить данные о бронировании может либо автор бронирования либо владелец вещи");
        }
        return bookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> getBookingByOwner(Long ownerId, String state) {
        BookingState bookingState;
        try {
            bookingState = BookingState.valueOf(state.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new NotFoundException("Доступные значения фильтра state: " + Arrays.toString(BookingState.values()));
        }
        List<BookingDto> bookings = bookingRepository.findByOwner(ownerId, bookingState.name()).stream()
                .map(bookingMapper::toBookingDto)
                .toList();
        if (bookings.isEmpty()) {
            throw new NotFoundException("Нет бронирований");
        }
        return bookings;
    }

    @Override
    public List<BookingDto> getBookingsByUser(Long userId, String state) {
        BookingState bookingState;
        try {
            bookingState = BookingState.valueOf(state.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new NotFoundException("Доступные значения фильтра state: " + Arrays.toString(BookingState.values()));
        }
        return bookingRepository.findByBooker(userId, bookingState.name()).stream()
                .map(bookingMapper::toBookingDto)
                .toList();
    }

    @Override
    @Transactional
    public BookingDto createBooking(Long bookerId, RequestBookingDto requestBookingDto) {
        requestBookingDto.setBookerId(bookerId);
        Booking booking = bookingMapper.toBooking(requestBookingDto);
        booking.setStatus(BookingStatus.WAITING);
        if (!booking.getItem().isAvailable()) {
            throw new ChekParamException(
                    String.format("Вещь с id = %d не доступна для аренды", booking.getItem().getId()));
        }
        return bookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingDto updateBookingStatus(Long ownerId, Long bookingId, Boolean approved) {
        Booking booking = findBookingById(bookingId);
        if (!ownerId.equals(booking.getItem().getOwner().getId())) {
            throw new ChekParamException("Изменить статус бронирования может только владелец вещи!");
        }
        booking.setStatus(Boolean.TRUE.equals(approved) ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        return bookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    private Booking findBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(String.format("Бронирование с id = %d не найдено", bookingId)));
    }
}
