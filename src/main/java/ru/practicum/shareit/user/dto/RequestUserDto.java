package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import ru.practicum.shareit.validation.NotBlankOrNull;
import ru.practicum.shareit.validation.ValidatorGroups;

@Getter
@Setter
@ToString
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class RequestUserDto {
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
