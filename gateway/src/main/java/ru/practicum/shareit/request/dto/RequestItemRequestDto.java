package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.validation.ValidatorGroups;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class RequestItemRequestDto {
    @NotBlank(groups = {ValidatorGroups.Create.class},
            message = "Текст описания не может быть пустым")
    private String description;
}