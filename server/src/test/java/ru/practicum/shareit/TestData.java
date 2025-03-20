package ru.practicum.shareit;

import ru.practicum.shareit.booking.dto.ShortBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemByRequestDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public class TestData {
    public static User getUser() {
        return new User(1L, "test1", "test1@yandex.ru");
    }

    public static User getUser2() {
        return new User(2L, "test2", "test2@yandex.ru");
    }

    public static User getWrongUser() {
        return new User(-1L, "test99999", "test99999@yandex.ru");
    }

    public static ItemRequest getItemRequest() {
        return new ItemRequest(
                1L,
                "description1",
                getUser2(),
                LocalDateTime.of(2025, 3, 24, 21, 24, 16));
    }

    public static ItemRequestWithItemsDto getItemRequestWithItemsDto() {
        Item item = getItem();
        ItemRequest itemRequest = getItemRequest();
        ItemByRequestDto itemByRequestDto = new ItemByRequestDto(
                item.getId(),
                item.getName(),
                item.getOwner().getId());
        return new ItemRequestWithItemsDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated(),
                List.of(itemByRequestDto));
    }

    public static Item getItem() {
        return new Item(1L, "name1", "description1", true, getUser(), getItemRequest());
    }

    public static Item getItem2() {
        return new Item(2L, "test2", "description2", false, getUser(), null);
    }

    public static ItemInfoDto getItemInfoDto() {
        Item item = getItem();
        List<Booking> bookings = getBookings();
        return new ItemInfoDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.isAvailable(),
                getShortBookingDto(bookings.getFirst()),
                getShortBookingDto(bookings.getLast()),
                List.of(getCommentDto(getComment())));
    }

    public static List<Booking> getBookings() {
        return List.of(
                new Booking(
                        1L,
                        LocalDateTime.of(2025, 3, 23, 21, 0, 0),
                        LocalDateTime.of(2025, 3, 24, 21, 0, 0),
                        getItem(),
                        getUser2(),
                        BookingStatus.APPROVED),
                new Booking(
                        2L,
                        LocalDateTime.of(2025, 4, 23, 21, 0, 0),
                        LocalDateTime.of(2025, 4, 24, 21, 0, 0),
                        getItem(),
                        getUser2(),
                        BookingStatus.APPROVED)
        );
    }

    public static Comment getComment() {
        return new Comment(
                1L,
                "text1",
                getItem(),
                getUser2(),
                LocalDateTime.of(2025, 3, 25, 12, 0, 0));
    }

    public static CommentDto getCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                comment.getCreated());
    }

    public static ShortBookingDto getShortBookingDto(Booking booking) {
        return new ShortBookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getBooker().getName());
    }

    public static List<Item> getItemsByOwner() {
        User user = getUser();
        return List.of(
                new Item(1L, "name1", "description1", true, user, getItemRequest()),
                new Item(2L, "test2", "description2", false, user, null));
    }

    public static List<ItemInfoDto> getItemsInfoDtoByOwner() {
        List<Booking> bookings = getBookings();
        return List.of(
                new ItemInfoDto(
                        1L,
                        "name1",
                        "description1",
                        true,
                        getShortBookingDto(bookings.getFirst()),
                        getShortBookingDto(bookings.getLast()),
                        List.of(getCommentDto(getComment()))),
                new ItemInfoDto(
                        2L,
                        "test2",
                        "description2",
                        false,
                        null,
                        null,
                        null));
    }

    public static List<Item> getFindByRequestIdIn() {
        return List.of(
                new Item(1L, "name1", "description1", true, getUser(), getItemRequest()));
    }

    public static List<Item> getSearchItem() {
        return List.of(
                new Item(1L, "name1", "description1", true, getUser(), getItemRequest()));
    }

    public static List<ItemRequest> getFindByRequestorIdOrderByCreatedDesc() {
        User user = getUser();
        return List.of(
                new ItemRequest(
                        3L,
                        "description3",
                        user,
                        LocalDateTime.of(2025, 3, 25, 8, 24, 50)),
                new ItemRequest(
                        2L,
                        "description2",
                        user,
                        LocalDateTime.of(2025, 3, 24, 21, 24, 16)));
    }

    public static List<ItemRequestWithItemsDto> getItemRequestsByUser() {
        return List.of(
                new ItemRequestWithItemsDto(
                        3L,
                        "description3",
                        LocalDateTime.of(2025, 3, 25, 8, 24, 50),
                        null),
                new ItemRequestWithItemsDto(
                        2L,
                        "description2",
                        LocalDateTime.of(2025, 3, 24, 21, 24, 16),
                        null));
    }
}
