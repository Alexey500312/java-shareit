package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.TestData;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingRepositoryTest {
    private final BookingRepository bookingRepository;

    @Test
    @DisplayName("Список бронирований по владельцу вещей и статусу")
    public void shouldFindByOwner() {
        User user = TestData.getUser();
        List<Booking> booking = TestData.getBookings();
        Optional<List<Booking>> bookingOptional = Optional.of(
                bookingRepository.findByOwner(user.getId(), BookingState.ALL.name()));

        assertThat(bookingOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(booking);
    }

    @Test
    @DisplayName("Список бронирований по пользователю и статусу")
    public void shouldFindByBooker() {
        User user = TestData.getUser2();
        List<Booking> booking = TestData.getBookings().stream()
                .sorted(Comparator.comparing(Booking::getStart).reversed())
                .toList();
        Optional<List<Booking>> bookingOptional = Optional.of(
                bookingRepository.findByBooker(user.getId(), BookingState.ALL.name()));

        assertThat(bookingOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(booking);
    }

    @Test
    @DisplayName("Поиск завершенного бронирования")
    public void shouldFindPastBooking() {
        User user = TestData.getUser2();
        Item item = TestData.getItem();
        Booking booking = TestData.getBookings().getFirst();
        Optional<Booking> bookingOptional = bookingRepository.findPastBooking(user.getId(), item.getId());

        assertThat(bookingOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(booking);
    }

    @Test
    @DisplayName("Получить последнее бронирование")
    public void shouldFindLastBooking() {
        User user = TestData.getUser();
        Item item = TestData.getItem();
        List<Booking> booking = List.of(TestData.getBookings().getFirst());
        Optional<List<Booking>> bookingOptional = Optional.of(
                bookingRepository.findLastBooking(user.getId(), List.of(item.getId())));

        assertThat(bookingOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(booking);
    }

    @Test
    @DisplayName("Получить следующее бронирование")
    public void shouldFindNextBooking() {
        User user = TestData.getUser();
        Item item = TestData.getItem();
        List<Booking> booking = List.of(TestData.getBookings().getLast());
        Optional<List<Booking>> bookingOptional = Optional.of(
                bookingRepository.findNextBooking(user.getId(), List.of(item.getId())));

        assertThat(bookingOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(booking);
    }

    @Test
    @DisplayName("Удалить бронирование по владельцу вещей")
    public void shouldDeleteByItemOwnerId() {
        User user = TestData.getUser();
        List<Booking> booking = TestData.getBookings();
        Optional<List<Booking>> bookingOptional = Optional.of(
                bookingRepository.findByOwner(user.getId(), BookingState.ALL.name()));

        assertThat(bookingOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(booking);

        bookingRepository.deleteByItemOwnerId(user.getId());

        bookingOptional = Optional.of(
                bookingRepository.findByOwner(user.getId(), BookingState.ALL.name()));

        assertThat(bookingOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(List.of());
    }
}
