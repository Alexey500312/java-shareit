package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.validation.StartBeforeEnd;
import ru.practicum.shareit.validation.ValidatorGroups;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@StartBeforeEnd(message = "Дата окончания бронирования должна быть больше даты начала")
public class RequestBookingDto implements StartEnd {
    private Long id;
    @NotNull(groups = {ValidatorGroups.Create.class},
            message = "Вещь не может быть пустой")
    private Long itemId;
    @NotNull(groups = {ValidatorGroups.Create.class},
            message = "Дата начала бронирования не может быть пустой")
    @FutureOrPresent(groups = {ValidatorGroups.Create.class},
            message = "Дата начала бронирования не может быть меньше текущей даты и времени")
    private LocalDateTime start;
    @NotNull(groups = {ValidatorGroups.Create.class},
            message = "Дата окончания бронирования не может быть пустой")
    @Future(groups = {ValidatorGroups.Create.class},
            message = "Дата окончания бронирования не может быть меньше текущей даты и времени")
    private LocalDateTime end;
    private Long bookerId;
}
