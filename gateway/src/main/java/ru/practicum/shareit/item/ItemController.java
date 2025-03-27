package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.RequestCommentDto;
import ru.practicum.shareit.item.dto.RequestItemDto;
import ru.practicum.shareit.validation.ValidatorGroups;

@Slf4j
@Validated
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private static final String HEADER_USER = "X-Sharer-User-Id";

    private final ItemClient itemClient;

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@RequestHeader(HEADER_USER) Long userId,
                                          @PathVariable("itemId") Long itemId) {
        log.info("Вызван метод GET /items/{}, userId={}", itemId, userId);
        return itemClient.getItem(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getItemByUser(@RequestHeader(HEADER_USER) Long userId) {
        log.info("Вызван метод GET /items, userId={}", userId);
        return itemClient.getItemByUser(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(@RequestHeader(HEADER_USER) Long userId,
                                             @RequestParam("text") String name) {
        log.info("Вызван метод GET /items/search?text={}, userId={}", name, userId);
        return itemClient.searchItem(userId, name);
    }

    @PostMapping
    @Validated({ValidatorGroups.Create.class})
    public ResponseEntity<Object> createItem(@RequestHeader(HEADER_USER) Long userId,
                                             @RequestBody @Valid RequestItemDto requestItemDto) {
        log.info("Вызван метод POST /items с телом {}, userId={}", requestItemDto, userId);
        return itemClient.createItem(userId, requestItemDto);
    }

    @PatchMapping("/{itemId}")
    @Validated({ValidatorGroups.Update.class})
    public ResponseEntity<Object> updateItem(@RequestHeader(HEADER_USER) Long userId,
                                             @PathVariable("itemId") Long itemId,
                                             @RequestBody @Valid RequestItemDto requestItemDto) {
        log.info("Вызван метод PATCH /items/{} с телом {}, userId={}", itemId, requestItemDto, userId);
        return itemClient.updateItem(userId, itemId, requestItemDto);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Object> deleteItem(@RequestHeader(HEADER_USER) Long userId,
                                             @PathVariable("itemId") Long itemId) {
        log.info("Вызван метод DELETE /items/{}, userId={}", itemId, userId);
        return itemClient.deleteItem(userId, itemId);
    }

    @PostMapping("/{itemId}/comment")
    @Validated({ValidatorGroups.Create.class})
    public ResponseEntity<Object> createComment(@RequestHeader(HEADER_USER) Long userId,
                                                @PathVariable("itemId") Long itemId,
                                                @RequestBody @Valid RequestCommentDto requestCommentDto) {
        log.info("Вызван метод POST /items/{}/comment с телом {}, userId={}", itemId, requestCommentDto, userId);
        return itemClient.createComment(userId, itemId, requestCommentDto);
    }
}
