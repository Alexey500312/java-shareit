package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import ru.practicum.shareit.validation.ValidatorGroups;

@Getter
@Setter
@ToString
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class RequestCommentDto {
    @NotBlank(groups = {ValidatorGroups.Create.class},
            message = "Текст комментария не может быть пустым")
    private String text;
}
