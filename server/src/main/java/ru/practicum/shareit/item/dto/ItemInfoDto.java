package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.ShortBookingDto;

import java.util.List;

@Data
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
