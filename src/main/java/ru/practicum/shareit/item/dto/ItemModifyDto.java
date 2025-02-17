package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.validation.NotBlankOrNull;
import ru.practicum.shareit.validation.ValidatorGroups;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class ItemModifyDto {
    @NotBlank(groups = {ValidatorGroups.Create.class},
            message = "Имя не может быть пустым")
    @NotBlankOrNull(groups = {ValidatorGroups.Update.class},
            message = "Имя не может быть пустым")
    private String name;
    @NotBlank(groups = {ValidatorGroups.Create.class},
            message = "Описание не может быть пустым")
    @NotBlankOrNull(groups = {ValidatorGroups.Update.class},
            message = "Описание не может быть пустым")
    private String description;
    @NotNull(groups = {ValidatorGroups.Create.class},
            message = "Статус доступности вещи для аренды не может быть пустым")
    private Boolean available;
}
