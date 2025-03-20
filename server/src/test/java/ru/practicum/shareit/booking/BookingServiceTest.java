package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.TestData;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.ChekParamException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceTest {
    private final BookingService bookingService;
    private final BookingMapper bookingMapper;
    private final UserMapper userMapper;

    @Test
    @DisplayName("Получить бронирование")
    public void shouldGetBooking() {
        User user = TestData.getUser();
        BookingDto bookingDtoTest = bookingMapper.toBookingDto(TestData.getBookings().getFirst());
        BookingDto bookingDto = bookingService.getBooking(user.getId(), bookingDtoTest.getId());

        assertEquals(bookingDtoTest, bookingDto);

        User user2 = TestData.getUser2();
        bookingDto = bookingService.getBooking(user2.getId(), bookingDtoTest.getId());

        assertEquals(bookingDtoTest, bookingDto);
        assertThrows(NotFoundException.class,
                () -> bookingService.getBooking(user2.getId(), -1L));
        assertThrows(ChekParamException.class,
                () -> bookingService.getBooking(-1L, bookingDtoTest.getId()));
    }

    @Test
    @DisplayName("Список бронирований по владельцу вещей и статусу")
    public void shouldGetBookingByOwner() {
        User user = TestData.getUser();
        List<BookingDto> bookingDtoTest = TestData.getBookings().stream()
                .map(bookingMapper::toBookingDto)
                .toList();
        Optional<List<BookingDto>> bookingDtoOptional = Optional.of(
                bookingService.getBookingByOwner(user.getId(), BookingState.ALL.name()));

        assertThat(bookingDtoOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(bookingDtoTest);
    }

    @Test
    @DisplayName("Список бронирований по пользователю и статусу")
    public void shouldGetBookingsByUser() {
        User user = TestData.getUser2();
        List<BookingDto> bookingDtoTest = TestData.getBookings().stream()
                .map(bookingMapper::toBookingDto)
                .sorted(Comparator.comparing(BookingDto::getStart).reversed())
                .toList();
        Optional<List<BookingDto>> bookingDtoOptional = Optional.of(
                bookingService.getBookingsByUser(user.getId(), BookingState.ALL.name()));

        assertThat(bookingDtoOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(bookingDtoTest);
        assertThrows(NotFoundException.class,
                () -> bookingService.getBookingByOwner(-1L, BookingState.ALL.name()));
    }

    @Test
    @DisplayName("Создать бронирование")
    public void shouldCreateBooking() {
        User user = TestData.getUser2();
        Item item = TestData.getItem();
        RequestBookingDto requestBookingDto = new RequestBookingDto(
                null,
                item.getId(),
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                user.getId());
        BookingDto bookingDto = bookingService.createBooking(user.getId(), requestBookingDto);

        assertEquals(requestBookingDto.getItemId(), bookingDto.getItem().getId());
        assertEquals(requestBookingDto.getBookerId(), bookingDto.getBooker().getId());
        assertEquals(requestBookingDto.getStart(), bookingDto.getStart());
        assertEquals(requestBookingDto.getEnd(), bookingDto.getEnd());
        assertEquals(BookingStatus.WAITING, bookingDto.getStatus());

        assertThrows(NotFoundException.class,
                () -> bookingService.createBooking(-1L, requestBookingDto));
        assertThrows(NotFoundException.class,
                () -> bookingService.createBooking(user.getId(), requestBookingDto.toBuilder()
                        .itemId(-1L)
                        .build()));
        assertThrows(ChekParamException.class,
                () -> bookingService.createBooking(user.getId(), requestBookingDto.toBuilder()
                        .itemId(TestData.getItem2().getId())
                        .build()));
    }

    @Test
    @DisplayName("Изменить статус бронирования")
    public void shouldUpdateBookingStatus() {
        User ownerItem = TestData.getUser();
        User user = TestData.getUser2();
        Item item = TestData.getItem();
        RequestBookingDto requestBookingDto = new RequestBookingDto(
                null,
                item.getId(),
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                user.getId());
        BookingDto bookingDto = bookingService.createBooking(user.getId(), requestBookingDto);

        assertThrows(NotFoundException.class,
                () -> bookingService.updateBookingStatus(user.getId(), -1L, true));
        assertThrows(ChekParamException.class,
                () -> bookingService.updateBookingStatus(user.getId(), bookingDto.getId(), true));

        BookingDto updateStatusBookingDto = bookingService.updateBookingStatus(ownerItem.getId(), bookingDto.getId(), true);

        assertEquals(BookingStatus.APPROVED, updateStatusBookingDto.getStatus());

        updateStatusBookingDto = bookingService.updateBookingStatus(ownerItem.getId(), bookingDto.getId(), false);

        assertEquals(BookingStatus.REJECTED, updateStatusBookingDto.getStatus());
    }
}
