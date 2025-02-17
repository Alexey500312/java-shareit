package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.validation.NotBlankOrNull;
import ru.practicum.shareit.validation.ValidatorGroups;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class UserModifyDto {
    @NotBlank(groups = {ValidatorGroups.Create.class},
            message = "Имя не может быть пустым")
    @NotBlankOrNull(groups = {ValidatorGroups.Update.class},
            message = "Имя не может быть пустым")
    private String name;
    @NotBlank(groups = {ValidatorGroups.Create.class},
            message = "Электронная почта не может быть пустой")
    @NotBlankOrNull(groups = {ValidatorGroups.Update.class},
            message = "Электронная почта не может быть пустой")
    @Email(groups = {ValidatorGroups.Create.class, ValidatorGroups.Update.class},
            message = "Электронная почта не соответствует формату")
    private String email;
}
