package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.RequestUserDto;
import ru.practicum.shareit.validation.ValidatorGroups;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserClient userClient;

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUser(@PathVariable("userId") Long userId) {
        log.info("Вызван метод GET /users/{}", userId);
        return userClient.getUser(userId);
    }

    @PostMapping
    @Validated({ValidatorGroups.Create.class})
    public ResponseEntity<Object> createUser(@RequestBody @Valid RequestUserDto requestUserDto) {
        log.info("Вызван метод POST /users с телом {}", requestUserDto);
        return userClient.createUser(requestUserDto);
    }

    @PatchMapping("/{userId}")
    @Validated({ValidatorGroups.Update.class})
    public ResponseEntity<Object> updateUser(@PathVariable("userId") Long userId,
                                             @RequestBody @Valid RequestUserDto requestUserDto) {
        log.info("Вызван метод PATCH /users/{} с телом {}", userId, requestUserDto);
        return userClient.updateUser(userId, requestUserDto);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable("userId") Long userId) {
        log.info("Вызван метод DELETE /users/{}", userId);
        return userClient.deleteUser(userId);
    }
}
