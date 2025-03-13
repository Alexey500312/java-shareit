package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.RequestBookingDto;

import java.util.List;

public interface BookingService {
    BookingDto getBooking(Long userId, Long bookingId);

    List<BookingDto> getBookingByOwner(Long ownerId, String state);

    List<BookingDto> getBookingsByUser(Long userId, String state);

    BookingDto createBooking(Long bookerId, RequestBookingDto requestBookingDto);

    BookingDto updateBookingStatus(Long ownerId, Long bookingId, Boolean approved);
}
