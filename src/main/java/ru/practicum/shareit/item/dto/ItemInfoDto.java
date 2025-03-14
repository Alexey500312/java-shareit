package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.booking.dto.ShortBookingDto;

import java.util.List;

@Getter
@Setter
@ToString
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ItemInfoDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private ShortBookingDto lastBooking;
    private ShortBookingDto nextBooking;
    private List<CommentDto> comments;
}
