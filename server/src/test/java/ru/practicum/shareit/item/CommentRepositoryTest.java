package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.TestData;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CommentRepositoryTest {
    private final CommentRepository commentRepository;

    @Test
    @DisplayName("Поиск комментариев по вещи")
    public void shouldFindByItemId() {
        Item item = TestData.getItem();
        List<Comment> commentTest = List.of(TestData.getComment());
        Optional<List<Comment>> commentOptional = Optional.of(commentRepository.findByItemId(item.getId()));

        assertThat(commentOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(commentTest);

        commentOptional = Optional.of(commentRepository.findByItemId(null));

        assertThat(commentOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(List.of());
    }

    @Test
    @DisplayName("Поиск комментариев по списку вещей")
    public void shouldFindByItemIdIn() {
        List<Long> ids = List.of(
                TestData.getItem().getId(),
                TestData.getItem2().getId());
        List<Comment> commentTest = List.of(TestData.getComment());
        Optional<List<Comment>> commentOptional = Optional.of(commentRepository.findByItemIdIn(ids));

        assertThat(commentOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(commentTest);

        commentOptional = Optional.of(commentRepository.findByItemIdIn(List.of()));

        assertThat(commentOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(List.of());
    }

    @Test
    @DisplayName("Удалить комментарии на вещи по владельцу")
    public void shouldDeleteByItemOwnerId() {
        Item item = TestData.getItem();
        List<Comment> commentTest = List.of(TestData.getComment());
        Optional<List<Comment>> commentOptional = Optional.of(commentRepository.findByItemId(item.getId()));

        assertThat(commentOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(commentTest);

        commentRepository.deleteByItemOwnerId(TestData.getUser().getId());

        commentOptional = Optional.of(commentRepository.findByItemId(item.getId()));

        assertThat(commentOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(List.of());
    }
}
