package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestItemRequestDto;
import ru.practicum.shareit.validation.ValidatorGroups;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private static final String HEADER_USER = "X-Sharer-User-Id";

    private final ItemRequestClient itemRequestClient;

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequest(@PathVariable("requestId") Long requestId) {
        log.info("Вызван метод GET /requests/{}", requestId);
        return itemRequestClient.getItemRequest(requestId);
    }

    @GetMapping
    public ResponseEntity<Object> getItemRequestByUser(@RequestHeader(HEADER_USER) Long userId) {
        log.info("Вызван метод GET /requests, userId={}", userId);
        return itemRequestClient.getItemRequestByUser(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getItemRequestAll(@RequestHeader(HEADER_USER) Long userId) {
        log.info("Вызван метод GET /requests/all, userId={}", userId);
        return itemRequestClient.getItemRequestAll(userId);
    }

    @PostMapping
    @Validated({ValidatorGroups.Create.class})
    public ResponseEntity<Object> createItemRequest(@RequestHeader(HEADER_USER) Long userId,
                                                    @RequestBody @Valid RequestItemRequestDto requestItemRequestDto) {
        log.info("Вызван метод POST /requests с телом {}, userId={}", requestItemRequestDto, userId);
        return itemRequestClient.createItemRequest(userId, requestItemRequestDto);
    }
}
