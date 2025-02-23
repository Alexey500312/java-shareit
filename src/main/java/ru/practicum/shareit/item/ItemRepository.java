package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    Optional<Item> getItem(Long itemId);

    List<Item> getItemByUser(Long userId);

    List<Item> getItems();

    List<Item> searchItem(String name);

    Item createItem(Item item);

    Item updateItem(Item item);

    void deleteItem(Item item);
}
