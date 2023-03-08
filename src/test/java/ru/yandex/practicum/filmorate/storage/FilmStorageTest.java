package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FilmStorageTest {

    private final FilmStorage filmStorage;
    private final GenreStorage genreStorage;
    private final RatingMPAStorage mpaStorage;

    @Test
    @Order(1)
    @DisplayName("FilmStorage - create")
    void create() {
        Film newFilm = Film.builder()
                .name("New Test Film")
                .description("Film Description")
                .duration(120)
                .releaseDate(LocalDate.of(2010, 10, 5))
                .mpa(mpaStorage.getById(1))
                .build();

        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.create(newFilm));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film -> {
                            assertThat(film).hasFieldOrPropertyWithValue("id", 5L);
                            assertThat(film).hasFieldOrPropertyWithValue("name", "New Test Film");
                            assertThat(film).hasFieldOrPropertyWithValue("description", "Film Description");
                            assertThat(film).hasFieldOrPropertyWithValue("duration", 120);
                            assertThat(film).hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(2010, 10, 5));
                            assertThat(film.getMpa()).hasFieldOrPropertyWithValue("id", 1L);
                        }
                );
    }

    @Test
    @Order(2)
    @DisplayName("FilmStorage - getAll")
    void getAll() {
        Optional<Collection<Film>> filmListOptional = Optional.ofNullable(filmStorage.getAll());
        assertThat(filmListOptional)
                .isPresent()
                .hasValueSatisfying(films ->
                {
                    assertThat(films).isNotEmpty();
                    assertThat(films).hasSize(5);
                    assertThat(films).element(0).hasFieldOrPropertyWithValue("id", 1L);
                    assertThat(films).element(0).hasFieldOrPropertyWithValue("name", "Один дома");
                    assertThat(films).element(1).hasFieldOrPropertyWithValue("id", 2L);
                    assertThat(films).element(1).hasFieldOrPropertyWithValue("name", "Аватар");
                    assertThat(films).element(2).hasFieldOrPropertyWithValue("id", 3L);
                    assertThat(films).element(2).hasFieldOrPropertyWithValue("name", "Шрэк");
                    assertThat(films).element(3).hasFieldOrPropertyWithValue("id", 4L);
                    assertThat(films).element(3).hasFieldOrPropertyWithValue("name", "Законопослушный гражданин");
                    assertThat(films).element(4).hasFieldOrPropertyWithValue("id", 5L);
                    assertThat(films).element(4).hasFieldOrPropertyWithValue("name", "New Test Film");
                });
    }

    @Test
    @Order(3)
    @DisplayName("FilmStorage - getById")
    void getFilmById() {
        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.getById(1L));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        {
                            assertThat(film).hasFieldOrPropertyWithValue("id", 1L);
                            assertThat(film).hasFieldOrPropertyWithValue("name", "Один дома");
                            assertThat(film).hasFieldOrPropertyWithValue("description", "Мальчик-озорник задает жару грабителям");
                            assertThat(film).hasFieldOrPropertyWithValue("duration", 103);
                            assertThat(film).hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(1990, 11, 10));
                            assertThat(film.getMpa()).hasFieldOrPropertyWithValue("id", 2L);
                            assertThat(film.getGenres()).hasSize(1);
                            assertThat(film.getGenres()).element(0).hasFieldOrPropertyWithValue("id", 1L);
                            assertThat(film.getLikes()).hasSize(3);
                            assertThat(film.getLikes()).element(0).isEqualTo(1L);
                            assertThat(film.getLikes()).element(1).isEqualTo(2L);
                            assertThat(film.getLikes()).element(2).isEqualTo(4L);
                        }
                );
    }

    @Test
    @Order(4)
    @DisplayName("FilmStorage - update")
    void update() {
        Film newFilm = Film.builder()
                .id(3L)
                .name("Updated Test Film")
                .description("Film Description")
                .duration(150)
                .releaseDate(LocalDate.of(2010, 1, 5))
                .mpa(mpaStorage.getById(2))
                .genres(Set.of(genreStorage.getById(1), genreStorage.getById(3)))
                .build();

        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.update(newFilm));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film -> {
                            assertThat(film).hasFieldOrPropertyWithValue("id", 3L);
                            assertThat(film).hasFieldOrPropertyWithValue("name", "Updated Test Film");
                            assertThat(film).hasFieldOrPropertyWithValue("description", "Film Description");
                            assertThat(film).hasFieldOrPropertyWithValue("duration", 150);
                            assertThat(film).hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(2010, 1, 5));
                            assertThat(film.getMpa()).hasFieldOrPropertyWithValue("id", 2L);
                            assertThat(film.getGenres()).hasSize(2);
                            assertThat(film.getGenres()).contains(genreStorage.getById(1), genreStorage.getById(3));
                        }
                );
    }

    @Test
    @Order(5)
    @DisplayName("FilmStorage - addLike")
    void addLike() {
        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.addLike(1L, 3L));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film -> {
                            assertThat(film.getLikes()).hasSize(4);
                            assertThat(film).hasFieldOrPropertyWithValue("id", 1L);
                            assertThat(film.getLikes()).element(0).isEqualTo(1L);
                            assertThat(film.getLikes()).element(1).isEqualTo(2L);
                            assertThat(film.getLikes()).element(2).isEqualTo(3L);
                            assertThat(film.getLikes()).element(3).isEqualTo(4L);
                        }
                );
    }

    @Test
    @Order(6)
    @DisplayName("FilmStorage - removeLike")
    void removeLike() {
        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.removeLike(1L, 3L));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film -> {
                            assertThat(film.getLikes()).hasSize(3);
                            assertThat(film).hasFieldOrPropertyWithValue("id", 1L);
                            assertThat(film.getLikes()).element(0).isEqualTo(1L);
                            assertThat(film.getLikes()).element(1).isEqualTo(2L);
                            assertThat(film.getLikes()).element(2).isEqualTo(4L);
                        }
                );
    }
}