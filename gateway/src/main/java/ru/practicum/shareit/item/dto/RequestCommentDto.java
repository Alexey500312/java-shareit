package ru.practicum.shareit.item.dto;

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
public class RequestCommentDto {
    @NotBlank(groups = {ValidatorGroups.Create.class},
            message = "Текст комментария не может быть пустым")
    private String text;
}
