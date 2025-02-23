package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemModifyDto;
import ru.practicum.shareit.validation.ValidatorGroups;

import java.util.List;

/**
 * ItemController
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto getItem(@PathVariable("itemId") Long itemId) {
        log.info("Вызван метод GET /items/{}", itemId);
        ItemDto itemDto = itemService.getItem(itemId);
        log.info("Метод GET /items/{} вернул ответ {}", itemId, itemDto);
        return itemDto;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> getItemByUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Вызван метод GET /items");
        List<ItemDto> itemDto = itemService.getItemByUser(userId);
        log.info("Метод GET /items вернул ответ {}", itemDto);
        return itemDto;
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
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @RequestBody @Valid ItemModifyDto itemModifyDto) {
        log.info("Вызван метод POST /items с телом {}", itemModifyDto);
        ItemDto newItemDto = itemService.createItem(userId, itemModifyDto);
        log.info("Метод POST /items вернул ответ {}", newItemDto);
        return newItemDto;
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    @Validated({ValidatorGroups.Update.class})
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @PathVariable("itemId") Long itemId,
                              @RequestBody @Valid ItemModifyDto itemModifyDto) {
        log.info("Вызван метод PATCH /items/{} с телом {}", itemId, itemModifyDto);
        ItemDto newItemDto = itemService.updateItem(userId, itemId, itemModifyDto);
        log.info("Метод PATCH /items/{} вернул ответ {}", itemId, newItemDto);
        return newItemDto;
    }

    @DeleteMapping("/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                           @PathVariable("itemId") Long itemId) {
        log.info("Вызван метод DELETE /items/{}", itemId);
        itemService.deleteItem(userId, itemId);
        log.info("Метод DELETE /items/{} успешно выполнен", itemId);
    }
}
