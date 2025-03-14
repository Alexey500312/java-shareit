package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.validation.ValidatorGroups;

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
        log.info("Вызван метод GET /items/{}", itemId);
        ItemInfoDto itemInfoDto = itemService.getItem(userId, itemId);
        log.info("Метод GET /items/{} вернул ответ {}", itemId, itemInfoDto);
        return itemInfoDto;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemInfoDto> getItemByUser(@RequestHeader(HEADER_USER) Long userId) {
        log.info("Вызван метод GET /items");
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
    @Validated({ValidatorGroups.Create.class})
    public ItemDto createItem(@RequestHeader(HEADER_USER) Long userId,
                              @RequestBody @Valid RequestItemDto itemModifyDto) {
        log.info("Вызван метод POST /items с телом {}", itemModifyDto);
        ItemDto newItemDto = itemService.createItem(userId, itemModifyDto);
        log.info("Метод POST /items вернул ответ {}", newItemDto);
        return newItemDto;
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    @Validated({ValidatorGroups.Update.class})
    public ItemDto updateItem(@RequestHeader(HEADER_USER) Long userId,
                              @PathVariable("itemId") Long itemId,
                              @RequestBody @Valid RequestItemDto itemModifyDto) {
        log.info("Вызван метод PATCH /items/{} с телом {}", itemId, itemModifyDto);
        ItemDto newItemDto = itemService.updateItem(userId, itemId, itemModifyDto);
        log.info("Метод PATCH /items/{} вернул ответ {}", itemId, newItemDto);
        return newItemDto;
    }

    @DeleteMapping("/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItem(@RequestHeader(HEADER_USER) Long userId,
                           @PathVariable("itemId") Long itemId) {
        log.info("Вызван метод DELETE /items/{}", itemId);
        itemService.deleteItem(userId, itemId);
        log.info("Метод DELETE /items/{} успешно выполнен", itemId);
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    @Validated({ValidatorGroups.Create.class})
    public CommentDto createComment(@RequestHeader(HEADER_USER) Long userId,
                                    @PathVariable("itemId") Long itemId,
                                    @RequestBody @Valid RequestCommentDto requestCommentDto) {
        log.info("Вызван метод POST /items/{}/comment", itemId);
        CommentDto commentDto = itemService.createComment(userId, itemId, requestCommentDto);
        log.info("Метод POST /items/{}/comment вернул ответ {}", itemId, commentDto);
        return commentDto;
    }
}
