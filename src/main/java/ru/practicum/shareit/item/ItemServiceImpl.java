package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.CRUDException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemModifyDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto getItem(Long itemId) {
        return ItemMapper.toItemDto(getItemById(itemId));
    }

    @Override
    public List<ItemDto> getItemByUser(Long userId) {
        return itemRepository.getItemByUser(userId).stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    @Override
    public List<ItemDto> searchItem(String name) {
        return name != null && !name.isBlank()
                ? itemRepository.searchItem(name).stream()
                .map(ItemMapper::toItemDto)
                .toList()
                : new ArrayList<>();
    }

    @Override
    public ItemDto createItem(Long userId, ItemModifyDto itemModifyDto) {
        User user = findUserById(userId);
        Item item = ItemMapper.toItem(itemModifyDto);
        item.setOwner(user);
        return ItemMapper.toItemDto(itemRepository.createItem(item));
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemModifyDto itemModifyDto) {
        User user = findUserById(userId);
        Item item = getItemById(itemId);
        if (!item.getOwner().getId().equals(user.getId())) {
            throw new CRUDException("Изменение доступно только для своих вещей");
        }
        Item newItem = new Item(
                itemId,
                itemModifyDto.getName() != null ? itemModifyDto.getName() : item.getName(),
                itemModifyDto.getDescription() != null ? itemModifyDto.getDescription() : item.getDescription(),
                itemModifyDto.getAvailable() != null ? itemModifyDto.getAvailable() : item.isAvailable(),
                item.getOwner(),
                item.getRequest());
        return ItemMapper.toItemDto(itemRepository.updateItem(newItem));
    }

    @Override
    public void deleteItem(Long userId, Long itemId) {
        User user = findUserById(userId);
        Item item = getItemById(itemId);
        if (!item.getOwner().getId().equals(user.getId())) {
            throw new CRUDException("Удаление доступно только для своих вещей");
        }
        itemRepository.deleteItem(item);
    }

    private Item getItemById(Long itemId) {
        return itemRepository.getItem(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Вещь с id = %d не найден", itemId)));
    }

    private User findUserById(Long userId) {
        return userRepository.findUserById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id = %d не найден", userId)));
    }
}
