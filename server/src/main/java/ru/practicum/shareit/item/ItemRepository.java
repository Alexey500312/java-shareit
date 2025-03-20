package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByOwnerId(Long userId);

    List<Item> findByRequestIdIn(List<Long> requestId);

    @Query("""
            select i from Item as i
            where i.available = true
            and (upper(i.name) like upper(concat('%', ?1, '%'))
            or upper(i.description) like upper(concat('%', ?1, '%')))
            """)
    List<Item> searchItem(String text);

    void deleteByOwnerId(Long userId);
}
