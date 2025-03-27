package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.dto.RequestItemRequestDto;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private static final String HEADER_USER = "X-Sharer-User-Id";

    private final ItemRequestService itemRequestService;

    @GetMapping("/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemRequestWithItemsDto getItemRequest(@PathVariable("requestId") Long requestId) {
        log.info("Вызван метод GET /requests/{}", requestId);
        ItemRequestWithItemsDto itemRequestWithItemsDto = itemRequestService.getItemById(requestId);
        log.info("Метод GET /requests/{} вернул ответ {}", requestId, itemRequestWithItemsDto);
        return itemRequestWithItemsDto;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemRequestWithItemsDto> getItemRequestByUser(@RequestHeader(HEADER_USER) Long userId) {
        log.info("Вызван метод GET /requests, userId={}", userId);
        List<ItemRequestWithItemsDto> itemRequestWithItemsDto = itemRequestService.getItemRequestsByUser(userId);
        log.info("Метод GET /requests вернул ответ {}", itemRequestWithItemsDto);
        return itemRequestWithItemsDto;
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemRequestDto> getItemRequestAll(@RequestHeader(HEADER_USER) Long userId) {
        log.info("Вызван метод GET /requests/all, userId={}", userId);
        List<ItemRequestDto> itemRequestDto = itemRequestService.getItemRequestsAll(userId);
        log.info("Метод GET /requests/all вернул ответ {}", itemRequestDto);
        return itemRequestDto;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestDto createItemRequest(@RequestHeader(HEADER_USER) Long userId,
                                            @RequestBody RequestItemRequestDto requestItemRequestDto) {
        log.info("Вызван метод POST /requests с телом {}, userId={}", requestItemRequestDto, userId);
        ItemRequestDto newItemRequestDto = itemRequestService.createItemRequest(userId, requestItemRequestDto);
        log.info("Метод POST /requests вернул ответ {}", newItemRequestDto);
        return newItemRequestDto;
    }
}
