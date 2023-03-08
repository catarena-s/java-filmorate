package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmGenreStorageTest {

    private final FilmGenreStorage filmGenreStorage;

    @Test
    @DisplayName("FriendStorage - getSetGenres for each popular film")
    void getSetGenresForTopFilms() {
        Optional<Map<Long, Set<Long>>> map = Optional.ofNullable(filmGenreStorage.getSetGenresForTopFilms(2));
        assertThat(map)
                .isPresent()
                .hasValueSatisfying(setMap -> {
                            assertThat(setMap).hasSize(2);
                            assertThat(setMap).containsKeys(1L, 3L);
                            assertThat(setMap.get(1L)).hasSize(1);
                            assertThat(setMap.get(3L)).hasSize(2);
                        }
                );
    }

    @Test
    @DisplayName("FriendStorage - getSetGenres for each film")
    void getSetGenresFroAllFilms() {
        Optional<Map<Long, Set<Long>>> map = Optional.ofNullable(filmGenreStorage.getSetGenresFroAllFilms());
        assertThat(map)
                .isPresent()
                .hasValueSatisfying(setMap -> {
                            assertThat(setMap).hasSize(4);
                            assertThat(setMap).containsKeys(1L, 2L, 3L, 4L);
                            assertThat(setMap.get(1L)).hasSize(1);
                            assertThat(setMap.get(2L)).hasSize(1);
                            assertThat(setMap.get(3L)).hasSize(2);
                            assertThat(setMap.get(4L)).hasSize(3);
                        }
                );
    }
}