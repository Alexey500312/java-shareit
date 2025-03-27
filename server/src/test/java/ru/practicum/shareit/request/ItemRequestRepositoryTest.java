package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.TestData;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestRepositoryTest {
    private final ItemRequestRepository itemRequestRepository;

    @Test
    @DisplayName("Список всех запросов созданных пользователем с сортировкой")
    public void shouldFindByRequestorIdOrderByCreatedDesc() {
        User user = TestData.getUser();
        List<ItemRequest> itemRequestTest = TestData.getFindByRequestorIdOrderByCreatedDesc();
        Optional<List<ItemRequest>> itemRequestOptional = Optional.of(
                itemRequestRepository.findByRequestorIdOrderByCreatedDesc(user.getId()));

        assertThat(itemRequestOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(itemRequestTest);

        user  = TestData.getUser2();
        itemRequestTest = List.of(TestData.getItemRequest());
        itemRequestOptional = Optional.of(itemRequestRepository.findByRequestorIdOrderByCreatedDesc(user.getId()));

        assertThat(itemRequestOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(itemRequestTest);
    }

    @Test
    @DisplayName("Список всех запросов кроме созданных пользователем с сортировкой")
    public void shouldFindByRequestorIdNotOrderByCreatedDesc() {
        User user  = TestData.getUser2();
        List<ItemRequest> itemRequestTest = TestData.getFindByRequestorIdOrderByCreatedDesc();
        Optional<List<ItemRequest>> itemRequestOptional = Optional.of(
                itemRequestRepository.findByRequestorIdNotOrderByCreatedDesc(user.getId()));

        assertThat(itemRequestOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(itemRequestTest);

        user  = TestData.getUser();
        itemRequestTest = List.of(TestData.getItemRequest());
        itemRequestOptional = Optional.of(itemRequestRepository.findByRequestorIdNotOrderByCreatedDesc(user.getId()));

        assertThat(itemRequestOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(itemRequestTest);
    }
}
