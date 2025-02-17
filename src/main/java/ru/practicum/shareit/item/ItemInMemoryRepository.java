package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Repository
public class ItemInMemoryRepository implements ItemRepository {
    private Long id = 0L;
    private final Map<Long, Item> items = new HashMap<>();

    @Override
    public Optional<Item> getItem(Long itemId) {
        return Optional.ofNullable(items.get(itemId));
    }

    @Override
    public List<Item> getItemByUser(Long userId) {
        return items.values().stream()
                .filter(i -> i.getOwner().getId().equals(userId))
                .toList();
    }

    @Override
    public List<Item> getItems() {
        return new ArrayList<>(items.values());
    }

    @Override
    public List<Item> searchItem(String name) {
        return items.values().stream()
                .filter(i -> i.getName().toUpperCase().contains(name.toUpperCase()) && i.isAvailable())
                .toList();
    }

    @Override
    public Item createItem(Item item) {
        item.setId(getId());
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item updateItem(Item item) {
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public void deleteItem(Item item) {
        items.remove(item.getId());
    }

    private Long getId() {
        return ++id;
    }
}
