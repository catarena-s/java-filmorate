package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreStorageTest {
    private final GenreStorage genreStorage;

    @Test
    @DisplayName("GenreStorage - getById")
    void genreById() {
        Optional<Genre> genreOptional = Optional.ofNullable(genreStorage.getById(1L));
        assertThat(genreOptional)
                .isPresent()
                .hasValueSatisfying(genre ->
                        {
                            assertThat(genre).hasFieldOrPropertyWithValue("id", 1L);
                            assertThat(genre).hasFieldOrPropertyWithValue("name", "Комедия");
                        }
                );
    }

    @Test
    @DisplayName("GenreStorage - getAll")
    void genreGetAll() {
        Optional<Collection<Genre>> genresOptional = Optional.ofNullable(genreStorage.getAll());
        assertThat(genresOptional)
                .isPresent()
                .hasValueSatisfying(genres ->
                        {
                            assertThat(genres).isNotEmpty();
                            assertThat(genres).hasSize(6);
                            assertThat(genres).element(0).hasFieldOrPropertyWithValue("id", 1L);
                            assertThat(genres).element(0).hasFieldOrPropertyWithValue("name", "Комедия");
                            assertThat(genres).element(1).hasFieldOrPropertyWithValue("id", 2L);
                            assertThat(genres).element(1).hasFieldOrPropertyWithValue("name", "Драма");
                            assertThat(genres).element(2).hasFieldOrPropertyWithValue("id", 3L);
                            assertThat(genres).element(2).hasFieldOrPropertyWithValue("name", "Мультфильм");
                            assertThat(genres).element(3).hasFieldOrPropertyWithValue("id", 4L);
                            assertThat(genres).element(3).hasFieldOrPropertyWithValue("name", "Триллер");
                            assertThat(genres).element(4).hasFieldOrPropertyWithValue("id", 5L);
                            assertThat(genres).element(4).hasFieldOrPropertyWithValue("name", "Документальный");
                            assertThat(genres).element(5).hasFieldOrPropertyWithValue("id", 6L);
                            assertThat(genres).element(5).hasFieldOrPropertyWithValue("name", "Боевик");
                        }
                );
    }

    @Test
    @DisplayName("GenreStorage - get genres bu filmId")
    void getByFilmId() {
        Optional<Collection<Genre>> genresOptional = Optional.ofNullable(genreStorage.getByFilmId(4L));
        assertThat(genresOptional)
                .isPresent()
                .hasValueSatisfying(genres ->
                {
                    assertThat(genres).isNotEmpty();
                    assertThat(genres).hasSize(3);
                    assertThat(genres).element(0).hasFieldOrPropertyWithValue("id", 2L);
                    assertThat(genres).element(0).hasFieldOrPropertyWithValue("name", "Драма");
                    assertThat(genres).element(1).hasFieldOrPropertyWithValue("id", 4L);
                    assertThat(genres).element(1).hasFieldOrPropertyWithValue("name", "Триллер");
                    assertThat(genres).element(2).hasFieldOrPropertyWithValue("id", 6L);
                    assertThat(genres).element(2).hasFieldOrPropertyWithValue("name", "Боевик");
                });
    }
}