package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private static final String HEADER_USER = "X-Sharer-User-Id";

    private final ItemService itemService;

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemInfoDto getItem(@RequestHeader(HEADER_USER) Long userId,
                               @PathVariable("itemId") Long itemId) {
        log.info("Вызван метод GET /items/{}, userId={}", itemId, userId);
        ItemInfoDto itemInfoDto = itemService.getItem(userId, itemId);
        log.info("Метод GET /items/{} вернул ответ {}", itemId, itemInfoDto);
        return itemInfoDto;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemInfoDto> getItemByUser(@RequestHeader(HEADER_USER) Long userId) {
        log.info("Вызван метод GET /items, userId={}", userId);
        List<ItemInfoDto> itemInfoDto = itemService.getItemByUser(userId);
        log.info("Метод GET /items вернул ответ {}", itemInfoDto);
        return itemInfoDto;
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> searchItem(@RequestParam("text") String name) {
        log.info("Вызван метод GET /items/search?text={}", name);
        List<ItemDto> itemDto = itemService.searchItem(name);
        log.info("Метод GET /items/search?text={} вернул ответ {}", name, itemDto);
        return itemDto;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto createItem(@RequestHeader(HEADER_USER) Long userId,
                              @RequestBody RequestItemDto requestItemDto) {
        log.info("Вызван метод POST /items с телом {}, userId={}", requestItemDto, userId);
        ItemDto newItemDto = itemService.createItem(userId, requestItemDto);
        log.info("Метод POST /items вернул ответ {}", newItemDto);
        return newItemDto;
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto updateItem(@RequestHeader(HEADER_USER) Long userId,
                              @PathVariable("itemId") Long itemId,
                              @RequestBody RequestItemDto requestItemDto) {
        log.info("Вызван метод PATCH /items/{} с телом {}, userId={}", itemId, requestItemDto, userId);
        ItemDto newItemDto = itemService.updateItem(userId, itemId, requestItemDto);
        log.info("Метод PATCH /items/{} вернул ответ {}", itemId, newItemDto);
        return newItemDto;
    }

    @DeleteMapping("/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItem(@RequestHeader(HEADER_USER) Long userId,
                           @PathVariable("itemId") Long itemId) {
        log.info("Вызван метод DELETE /items/{}, userId={}", itemId, userId);
        itemService.deleteItem(userId, itemId);
        log.info("Метод DELETE /items/{} успешно выполнен", itemId);
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(@RequestHeader(HEADER_USER) Long userId,
                                    @PathVariable("itemId") Long itemId,
                                    @RequestBody RequestCommentDto requestCommentDto) {
        log.info("Вызван метод POST /items/{}/comment с телом {}, userId={}", itemId, requestCommentDto, userId);
        CommentDto commentDto = itemService.createComment(userId, itemId, requestCommentDto);
        log.info("Метод POST /items/{}/comment вернул ответ {}", itemId, commentDto);
        return commentDto;
    }
}
