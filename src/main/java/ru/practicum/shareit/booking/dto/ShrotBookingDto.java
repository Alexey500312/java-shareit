package ru.practicum.shareit.booking.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ShrotBookingDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private String bookerName;
}
