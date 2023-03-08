package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class LikeStorageTest {
    private final LikeStorage likeStorage;

    @Test
    @DisplayName("LikeStorage - getUsersLikeId by filmId")
    void getUsersIDLikesFilm() {
        Optional<List<Long>> userIdOptional = Optional.ofNullable(likeStorage.getUsersLikeId(1L));
        assertThat(userIdOptional)
                .isPresent()
                .hasValueSatisfying(users -> {
                            assertThat(users).isNotEmpty();
                            assertThat(users).hasSize(3);
                            assertThat(users).contains(1L, 2L, 4L);
                        }
                );
    }

    @Test
    @DisplayName("LikeStorage - getSetLikes for top films")
    void getSetLikesForTopFilms() {
        Optional<Map<Long, Set<Long>>> map = Optional.ofNullable(likeStorage.getSetLikesForTopFilms(2));
        assertThat(map)
                .isPresent()
                .hasValueSatisfying(setMap -> {
                            assertThat(setMap).hasSize(2);
                            assertThat(setMap).containsKeys(1L, 3L);
                            assertThat(setMap.get(1L)).hasSize(3);
                            assertThat(setMap.get(1L)).contains(1L, 2L, 4L);
                            assertThat(setMap.get(3L)).hasSize(4);
                            assertThat(setMap.get(3L)).contains(1L, 2L, 3L, 4L);
                        }
                );
    }

    @Test
    @DisplayName("LikeStorage - getSetLikes for each film")
    void getSetLikesForAllFilms() {
        Optional<Map<Long, Set<Long>>> map = Optional.ofNullable(likeStorage.getSetLikesForAllFilms());
        assertThat(map)
                .isPresent()
                .hasValueSatisfying(setMap -> {
                            assertThat(setMap).hasSize(4);
                            assertThat(setMap).containsKeys(1L, 2L, 3L, 4L);
                            assertThat(setMap.get(1L)).hasSize(3);
                            assertThat(setMap.get(1L)).contains(1L, 2L, 4L);
                            assertThat(setMap.get(2L)).hasSize(1);
                            assertThat(setMap.get(2L)).contains(1L);
                            assertThat(setMap.get(3L)).hasSize(4);
                            assertThat(setMap.get(3L)).contains(1L, 2L, 3L, 4L);
                            assertThat(setMap.get(4L)).hasSize(2);
                            assertThat(setMap.get(4L)).contains(3L, 4L);
                        }
                );
    }

    @Test
    @DisplayName("LikeStorage - check there is Like from the user to the film")
    void isNotExistLikeFromUser() {
        assertFalse(likeStorage.isNotExistLikeFromUSer(1L, 1L));
        assertFalse(likeStorage.isNotExistLikeFromUSer(1L, 2L));
        assertTrue(likeStorage.isNotExistLikeFromUSer(1L, 3L));
    }

}