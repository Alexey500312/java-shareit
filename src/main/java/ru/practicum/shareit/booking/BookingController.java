package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.validation.ValidatorGroups;

import java.util.List;

/**
 * BookingController
 */
@Slf4j
@Validated
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private static final String HEADER_USER = "X-Sharer-User-Id";
    private final BookingService bookingService;

    @GetMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto getBooking(@RequestHeader(HEADER_USER) Long userId,
                                 @PathVariable("bookingId") Long bookingId) {
        log.info("Вызван метод GET /bookings/{}", bookingId);
        BookingDto newBookingDto = bookingService.getBooking(userId, bookingId);
        log.info("Метод GET /bookings/{} вернул ответ {}", bookingId, newBookingDto);
        return newBookingDto;
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> getBookingByOwner(@RequestHeader(HEADER_USER) Long ownerId,
                                              @RequestParam(value = "state", defaultValue = "ALL") String state) {
        log.info("Вызван метод GET /bookings/owner");
        List<BookingDto> bookingDto = bookingService.getBookingByOwner(ownerId, state);
        log.info("Метод GET /bookings/owner вернул ответ {}", bookingDto);
        return bookingDto;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> getBookingsByUser(@RequestHeader(HEADER_USER) Long userId,
                                              @RequestParam(value = "state", defaultValue = "ALL") String state) {
        log.info("Вызван метод GET /bookings");
        List<BookingDto> bookingDto = bookingService.getBookingsByUser(userId, state);
        log.info("Метод GET /bookings вернул ответ {}", bookingDto);
        return bookingDto;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Validated({ValidatorGroups.Create.class})
    public BookingDto createBooking(@RequestHeader(HEADER_USER) Long userId,
                                    @RequestBody @Valid RequestBookingDto requestBookingDto) {
        log.info("Вызван метод POST /bookings с телом {}", requestBookingDto);
        BookingDto newBookingDto = bookingService.createBooking(userId, requestBookingDto);
        log.info("Метод POST /bookings вернул ответ {}", newBookingDto);
        return newBookingDto;
    }

    @PatchMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto updateBookingStatus(@RequestHeader(HEADER_USER) Long userId,
                                          @PathVariable("bookingId") Long bookingId,
                                          @RequestParam(value = "approved", defaultValue = "true") Boolean approved) {
        log.info("Вызван метод PATCH /bookings/{}", bookingId);
        BookingDto newBookingDto = bookingService.updateBookingStatus(userId, bookingId, approved);
        log.info("Метод PATCH /bookings/{} вернул ответ {}", bookingId, newBookingDto);
        return newBookingDto;
    }
}
