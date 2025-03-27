package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.TestData;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserRepositoryTest {
    private final UserRepository userRepository;

    @Test
    @DisplayName("Поиск пользователя по email")
    public void shouldFindByEmailContainsIgnoreCase() {
        User testUser = TestData.getUser();
        Optional<User> userOptional = userRepository.findByEmailContainsIgnoreCase(testUser.getEmail());

        assertThat(userOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(testUser);

        assertThrows(NotFoundException.class,
                () -> userRepository.findByEmailContainsIgnoreCase(TestData.getWrongUser().getEmail())
                        .orElseThrow(() -> new NotFoundException("Пользователь не найден")));
    }
}
