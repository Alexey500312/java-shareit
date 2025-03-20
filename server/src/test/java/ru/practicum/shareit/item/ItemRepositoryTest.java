package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.TestData;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRepositoryTest {
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Test
    @DisplayName("Поиск вещи по владельцу")
    public void shouldFindByOwnerId() {
        User user = TestData.getUser();
        List<Item> items = TestData.getItemsByOwner();

        Optional<List<Item>> itemsOptional = Optional.of(itemRepository.findByOwnerId(user.getId()));

        assertThat(itemsOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(items);
    }

    @Test
    @DisplayName("Поиск вещи по запросу")
    public void shouldFindByRequestIdIn() {
        User user = TestData.getUser2();
        List<Item> items = TestData.getFindByRequestIdIn();

        Optional<List<Item>> itemsOptional = Optional.of(
                itemRepository.findByRequestIdIn(List.of(TestData.getItemRequest().getId())));

        assertThat(itemsOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(items);
    }

    @Test
    @DisplayName("Поиск вещи по наименованию или описанию")
    public void shouldSearchItem() {
        List<Item> items = TestData.getSearchItem();

        Optional<List<Item>> itemsOptional = Optional.of(itemRepository.searchItem("DeScR"));

        assertThat(itemsOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(items);
    }

    @Test
    @DisplayName("Удаление вещей по владельцу")
    public void shouldDeleteByOwnerId() {
        User user = TestData.getUser();
        List<Item> items = TestData.getItemsByOwner();

        Optional<List<Item>> itemsOptional = Optional.of(itemRepository.findByOwnerId(user.getId()));

        assertThat(itemsOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(items);

        commentRepository.deleteByItemOwnerId(user.getId());
        bookingRepository.deleteByItemOwnerId(user.getId());
        itemRepository.deleteByOwnerId(user.getId());
        itemsOptional = Optional.of(itemRepository.findByOwnerId(user.getId()));

        assertThat(itemsOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(List.of());
    }
}
