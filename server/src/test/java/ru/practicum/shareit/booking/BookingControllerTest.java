package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.TestData;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.ChekParamException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
@Import({BookingMapper.class, UserMapper.class, ItemMapper.class})
public class BookingControllerTest {
    private static final String HEADER_USER = "X-Sharer-User-Id";

    @MockBean
    private BookingService bookingService;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private BookingMapper bookingMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ObjectMapper mapper;

    @Test
    @DisplayName("Получить бронирование")
    public void shouldGetBooking() throws Exception {
        User user = TestData.getUser();
        BookingDto bookingDto = bookingMapper.toBookingDto(TestData.getBookings().getFirst());

        when(bookingService.getBooking(user.getId(), bookingDto.getId())).thenReturn(bookingDto);

        mvc.perform(get("/bookings/" + bookingDto.getId())
                        .header(HEADER_USER, user.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookingDto)));

        User user2 = TestData.getUser2();

        when(bookingService.getBooking(user2.getId(), bookingDto.getId())).thenReturn(bookingDto);

        mvc.perform(get("/bookings/" + bookingDto.getId())
                        .header(HEADER_USER, user2.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookingDto)));

        when(bookingService.getBooking(-1L, bookingDto.getId())).thenThrow(NotFoundException.class);

        mvc.perform(get("/bookings/" + bookingDto.getId())
                        .header(HEADER_USER, -1L))
                .andExpect(status().is4xxClientError());

        when(bookingService.getBooking(user2.getId(), -1L)).thenThrow(ChekParamException.class);

        mvc.perform(get("/bookings/" + -1L)
                        .header(HEADER_USER, user2.getId()))
                .andExpect(status().is4xxClientError());

        verify(bookingService, times(1)).getBooking(user.getId(), bookingDto.getId());
        verify(bookingService, times(1)).getBooking(user2.getId(), bookingDto.getId());
        verify(bookingService, times(1)).getBooking(-1L, bookingDto.getId());
        verify(bookingService, times(1)).getBooking(user2.getId(), -1L);
    }

    @Test
    @DisplayName("Список бронирований по владельцу вещей и статусу")
    public void shouldGetBookingByOwner() throws Exception {
        User user = TestData.getUser();
        List<BookingDto> bookingDto = TestData.getBookings().stream()
                .map(bookingMapper::toBookingDto)
                .toList();

        when(bookingService.getBookingByOwner(user.getId(), BookingState.ALL.name())).thenReturn(bookingDto);

        mvc.perform(get("/bookings/owner")
                        .header(HEADER_USER, user.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookingDto)));

        verify(bookingService, times(1)).getBookingByOwner(user.getId(), BookingState.ALL.name());
    }

    @Test
    @DisplayName("Список бронирований по пользователю и статусу")
    public void shouldGetBookingsByUser() throws Exception {
        User user = TestData.getUser2();
        List<BookingDto> bookingDto = TestData.getBookings().stream()
                .map(bookingMapper::toBookingDto)
                .sorted(Comparator.comparing(BookingDto::getStart).reversed())
                .toList();

        when(bookingService.getBookingsByUser(user.getId(), BookingState.ALL.name())).thenReturn(bookingDto);

        mvc.perform(get("/bookings")
                        .header(HEADER_USER, user.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookingDto)));

        verify(bookingService, times(1)).getBookingsByUser(user.getId(), BookingState.ALL.name());
    }

    @Test
    @DisplayName("Создать бронирование")
    public void shouldCreateBooking() throws Exception {
        User user = TestData.getUser2();
        Item item = TestData.getItem();
        RequestBookingDto requestBookingDto = new RequestBookingDto(
                null,
                item.getId(),
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                user.getId());
        BookingDto bookingDto = bookingMapper.toBookingDto(
                        bookingMapper.toBooking(requestBookingDto, user, item)).toBuilder()
                .status(BookingStatus.WAITING)
                .build();

        when(bookingService.createBooking(user.getId(), requestBookingDto)).thenReturn(bookingDto);

        mvc.perform(post("/bookings")
                        .header(HEADER_USER, user.getId())
                        .content(mapper.writeValueAsString(requestBookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(mapper.writeValueAsString(bookingDto)));

        when(bookingService.createBooking(-1L, requestBookingDto)).thenThrow(NotFoundException.class);

        mvc.perform(post("/bookings")
                        .header(HEADER_USER, -1L)
                        .content(mapper.writeValueAsString(requestBookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        when(bookingService.createBooking(user.getId(), requestBookingDto.toBuilder()
                .itemId(-1L)
                .build())).thenThrow(NotFoundException.class);

        mvc.perform(post("/bookings")
                        .header(HEADER_USER, user.getId())
                        .content(mapper.writeValueAsString(requestBookingDto.toBuilder()
                                .itemId(-1L)
                                .build()))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        Item item2 = TestData.getItem2();

        when(bookingService.createBooking(user.getId(), requestBookingDto.toBuilder()
                .itemId(item2.getId())
                .build())).thenThrow(ChekParamException.class);

        mvc.perform(post("/bookings")
                        .header(HEADER_USER, user.getId())
                        .content(mapper.writeValueAsString(requestBookingDto.toBuilder()
                                .itemId(item2.getId())
                                .build()))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        verify(bookingService, times(1)).createBooking(user.getId(), requestBookingDto);
        verify(bookingService, times(1)).createBooking(-1L, requestBookingDto);
        verify(bookingService, times(1)).createBooking(user.getId(), requestBookingDto.toBuilder()
                .itemId(-1L)
                .build());
        verify(bookingService, times(1)).createBooking(user.getId(), requestBookingDto.toBuilder()
                .itemId(item2.getId())
                .build());
    }

    @Test
    @DisplayName("Изменить статус бронирования")
    public void shouldUpdateBookingStatus() throws Exception {
        User ownerItem = TestData.getUser();
        User user = TestData.getUser2();
        Item item = TestData.getItem();
        RequestBookingDto requestBookingDto = new RequestBookingDto(
                999999L,
                item.getId(),
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                user.getId());
        BookingDto bookingDto = bookingMapper.toBookingDto(
                        bookingMapper.toBooking(requestBookingDto, user, item)).toBuilder()
                .id(999999L)
                .status(BookingStatus.WAITING)
                .build();

        when(bookingService.updateBookingStatus(user.getId(), -1L, true))
                .thenThrow(NotFoundException.class);

        mvc.perform(patch("/bookings/" + -1L)
                        .header(HEADER_USER, user.getId())
                        .param("approved", String.valueOf(true))
                        .content(mapper.writeValueAsString(requestBookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        when(bookingService.updateBookingStatus(user.getId(), bookingDto.getId(), true))
                .thenThrow(ChekParamException.class);

        mvc.perform(patch("/bookings/" + bookingDto.getId())
                        .header(HEADER_USER, user.getId())
                        .param("approved", String.valueOf(true))
                        .content(mapper.writeValueAsString(requestBookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        BookingDto updateStatusBookingDto = bookingDto.toBuilder()
                .status(BookingStatus.APPROVED)
                .build();

        when(bookingService.updateBookingStatus(ownerItem.getId(), updateStatusBookingDto.getId(), true))
                .thenReturn(updateStatusBookingDto);

        mvc.perform(patch("/bookings/" + updateStatusBookingDto.getId())
                        .header(HEADER_USER, ownerItem.getId())
                        .param("approved", String.valueOf(true))
                        .content(mapper.writeValueAsString(updateStatusBookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(updateStatusBookingDto)));

        updateStatusBookingDto = bookingDto.toBuilder()
                .status(BookingStatus.REJECTED)
                .build();

        when(bookingService.updateBookingStatus(ownerItem.getId(), updateStatusBookingDto.getId(), false))
                .thenReturn(updateStatusBookingDto);

        mvc.perform(patch("/bookings/" + updateStatusBookingDto.getId())
                        .header(HEADER_USER, ownerItem.getId())
                        .param("approved", String.valueOf(false))
                        .content(mapper.writeValueAsString(updateStatusBookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(updateStatusBookingDto)));

        verify(bookingService, times(1)).updateBookingStatus(user.getId(), -1L, true);
        verify(bookingService, times(1)).updateBookingStatus(user.getId(), bookingDto.getId(), true);
        verify(bookingService, times(1)).updateBookingStatus(ownerItem.getId(), updateStatusBookingDto.getId(), true);
        verify(bookingService, times(1)).updateBookingStatus(ownerItem.getId(), updateStatusBookingDto.getId(), false);
    }
}
