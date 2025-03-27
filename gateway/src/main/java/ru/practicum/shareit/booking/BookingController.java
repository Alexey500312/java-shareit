package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.validation.ValidatorGroups;

import java.util.Arrays;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private static final String HEADER_USER = "X-Sharer-User-Id";
    private final BookingClient bookingClient;

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader(HEADER_USER) Long userId,
                                             @PathVariable("bookingId") Long bookingId) {
        log.info("Вызван метод GET /bookings/{}, userId={}", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingByOwner(@RequestHeader(HEADER_USER) Long ownerId,
                                                    @RequestParam(value = "state", defaultValue = "ALL") String stateParam) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Доступные значения фильтра state: " + Arrays.toString(BookingState.values())));
        log.info("Вызван метод GET /bookings/owner с параметрами state {}, userId={}", stateParam, ownerId);
        return bookingClient.getBookingsByOwner(ownerId, state);
    }

    @GetMapping
    public ResponseEntity<Object> getBookingsByUser(@RequestHeader(HEADER_USER) Long userId,
                                                    @RequestParam(value = "state", defaultValue = "ALL") String stateParam) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Доступные значения фильтра state: " + Arrays.toString(BookingState.values())));
        log.info("Вызван метод GET /bookings с параметрами state {}, userId={}", stateParam, userId);
        return bookingClient.getBookingsByUser(userId, state);
    }

    @PostMapping
    @Validated({ValidatorGroups.Create.class})
    public ResponseEntity<Object> createBooking(@RequestHeader(HEADER_USER) Long userId,
                                                @RequestBody @Valid RequestBookingDto requestBookingDto) {
        log.info("Вызван метод POST /bookings с телом {}, userId={}", requestBookingDto, userId);
        return bookingClient.createBooking(userId, requestBookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateBookingStatus(@RequestHeader(HEADER_USER) Long userId,
                                                      @PathVariable("bookingId") Long bookingId,
                                                      @RequestParam(value = "approved", defaultValue = "true") Boolean approved) {
        log.info("Вызван метод PATCH /bookings/{}, approved {}, userId={}", bookingId, approved, userId);
        return bookingClient.updateBookingStatus(userId, bookingId, approved);
    }
}
