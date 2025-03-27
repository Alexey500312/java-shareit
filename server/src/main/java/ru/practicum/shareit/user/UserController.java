package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.RequestUserDto;
import ru.practicum.shareit.user.dto.UserDto;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUser(@PathVariable("userId") Long userId) {
        log.info("Вызван метод GET /users/{}", userId);
        UserDto userDto = userService.getUser(userId);
        log.info("Метод GET /users/{} вернул ответ {}", userId, userDto);
        return userDto;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody RequestUserDto requestUserDto) {
        log.info("Вызван метод POST /users с телом {}", requestUserDto);
        UserDto newUserDto = userService.createUser(requestUserDto);
        log.info("Метод POST /users вернул ответ {}", newUserDto);
        return newUserDto;
    }

    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto updateUser(@PathVariable("userId") Long userId,
                              @RequestBody RequestUserDto requestUserDto) {
        log.info("Вызван метод PATCH /users/{} с телом {}", userId, requestUserDto);
        UserDto newUserDto = userService.updateUser(userId, requestUserDto);
        log.info("Метод PATCH /users/{} вернул ответ {}", userId, newUserDto);
        return newUserDto;
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("userId") Long userId) {
        log.info("Вызван метод DELETE /users/{}", userId);
        userService.deleteUser(userId);
        log.info("Метод DELETE /users/{} успешно выполнен", userId);
    }
}
