package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FilmorateApplicationTests {
    private final FilmStorage filmStorage;

    private final UserStorage userStorage;
    private final LikeStorage likeStorage;
    private final GenreStorage genreStorage;
    private final RatingMPAStorage mpaStorage;
    private final FriendStorage friendStorage;

    //------------------------------------------------------------------------------------------------------------------
    //---UserStorage----------------------------------------------------------------------------------------------------
    @Test
    @Order(1)
    @DisplayName("UserStorage - create")
    void testCreateUSer() {
        User newUser = User.builder()
                .login("test_user")
                .name("Test User")
                .birthday(LocalDate.of(2010, 10, 5))
                .email("testUser@email.com")
                .build();

        Optional<User> userOptional = Optional.ofNullable(userStorage.create(newUser));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user -> {
                            assertThat(user).hasFieldOrPropertyWithValue("id", 4L);
                            assertThat(user).hasFieldOrPropertyWithValue("login", "test_user");
                            assertThat(user).hasFieldOrPropertyWithValue("name", "Test User");
                            assertThat(user).hasFieldOrPropertyWithValue("email", "testUser@email.com");
                            assertThat(user).hasFieldOrPropertyWithValue("birthday",
                                    LocalDate.of(2010, 10, 5));
                        }
                );
    }

    @Test
    @Order(2)
    @DisplayName("UserStorage - getAll")
    void testGetAllUsers() {
        Optional<Collection<User>> userList = Optional.ofNullable(userStorage.getAll());
        assertThat(userList)
                .isPresent()
                .hasValueSatisfying(user ->
                {
                    assertThat(user).isNotEmpty();
                    assertThat(user).element(0).hasFieldOrPropertyWithValue("id", 1L);
                    assertThat(user).element(1).hasFieldOrPropertyWithValue("id", 2L);
                    assertThat(user).element(2).hasFieldOrPropertyWithValue("id", 3L);
                    assertThat(user).element(3).hasFieldOrPropertyWithValue("id", 4L);
                    assertThat(user).hasSize(4);
                });
    }

    @Test
    @Order(3)
    @DisplayName("UserStorage - update")
    void testUpdateUser() {
        User newUser = User.builder()
                .id(4)
                .login("test_user4")
                .name("New name User4")
                .birthday(LocalDate.of(2010, 10, 5))
                .email("testUser@email.com")
                .build();

        Optional<User> userOptional = Optional.ofNullable(userStorage.update(newUser));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user -> {
                            assertThat(user).hasFieldOrPropertyWithValue("id", 4L);
                            assertThat(user).hasFieldOrPropertyWithValue("login", "test_user4");
                            assertThat(user).hasFieldOrPropertyWithValue("name", "New name User4");
                            assertThat(user).hasFieldOrPropertyWithValue("email", "testUser@email.com");
                            assertThat(user).hasFieldOrPropertyWithValue("birthday", LocalDate.of(2010, 10, 5));
                        }
                );
    }

    @Test
    @Order(4)
    @DisplayName("UserStorage - getFriends by userId")
    void testGetUserFriends() {
        Optional<List<User>> usersOptional = Optional.ofNullable(userStorage.getFriendsForUser(1L));
        assertThat(usersOptional)
                .isPresent()
                .hasValueSatisfying(users -> {
                            assertThat(users).isNotEmpty();
                            assertThat(users).hasSize(2);
                            assertThat(users).first().hasFieldOrPropertyWithValue("id", 2L);
                            assertThat(users).element(1).hasFieldOrPropertyWithValue("id", 3L);
                        }
                );
    }

    @Test
    @Order(5)
    @DisplayName("UserStorage - getCommonFriends")
    void getCommonFriends() {
        Optional<List<User>> commonFriendsOptional = Optional.ofNullable(userStorage.getCommonFriends(1L, 3L));
        assertThat(commonFriendsOptional)
                .isPresent()
                .hasValueSatisfying(users -> {
                            assertThat(users).isNotEmpty();
                            assertThat(users).hasSize(1);
                            assertThat(users).element(0).hasFieldOrPropertyWithValue("id", 2L);
                        }
                );
    }

    @Test
    @Order(6)
    @DisplayName("UserStorage - getById")
    void testFindUserById() {
        Optional<User> userOptional = Optional.ofNullable(userStorage.getById(1L));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        {
                            assertThat(user).hasFieldOrPropertyWithValue("id", 1L);
                            assertThat(user).hasFieldOrPropertyWithValue("login", "dolore");
                            assertThat(user).hasFieldOrPropertyWithValue("name", "est dolore");
                            assertThat(user).hasFieldOrPropertyWithValue("email", "mail@yandex.ru");
                            assertThat(user).hasFieldOrPropertyWithValue("birthday", LocalDate.of(2001, 5, 5));

                        }
                );
    }

    @Test
    @Order(7)
    @DisplayName("UserStorage - addFriend")
    void testAddFriend() {
        Optional<User> userOptional = Optional.ofNullable(userStorage.addFriend(3L, 1L));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user -> {
                            assertThat(user).hasFieldOrPropertyWithValue("id", 3L);
                            assertThat(user.getFriends()).isNotEmpty();
                            assertThat(user.getFriends()).hasSize(2);
                        }
                );
    }

    @Test
    @Order(8)
    @DisplayName("UserStorage - removeFromFriends")
    void removeFromFriends() {
        Optional<User> userOptional = Optional.ofNullable(userStorage.removeFromFriends(1L, 2L));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user -> {
                            assertThat(user.getFriends()).hasSize(1);
                            assertThat(user).hasFieldOrPropertyWithValue("id", 1L);
                            assertThat(user.getFriends()).isNotEmpty();
                        }
                );
    }

    //------------------------------------------------------------------------------------------------------------------
    //---FilmStorage----------------------------------------------------------------------------------------------------
    @Test
    @Order(9)
    @DisplayName("FilmStorage - create")
    void testCreate() {
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
                            assertThat(film).hasFieldOrPropertyWithValue("id", 3L);
                            assertThat(film).hasFieldOrPropertyWithValue("name", "New Test Film");
                            assertThat(film).hasFieldOrPropertyWithValue("description", "Film Description");
                            assertThat(film).hasFieldOrPropertyWithValue("duration", 120);
                            assertThat(film).hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(2010, 10, 5));
                            assertThat(film.getMpa()).hasFieldOrPropertyWithValue("id", 1L);
                        }
                );
    }

    @Test
    @Order(10)
    @DisplayName("FilmStorage - getAll")
    void testGetAll() {
        Optional<Collection<Film>> filmListOptional = Optional.ofNullable(filmStorage.getAll());
        assertThat(filmListOptional)
                .isPresent()
                .hasValueSatisfying(films ->
                {
                    assertThat(films).isNotEmpty();
                    assertThat(films).hasSize(3);
                    assertThat(films).element(0).hasFieldOrPropertyWithValue("id", 1L);
                    assertThat(films).element(1).hasFieldOrPropertyWithValue("id", 2L);
                    assertThat(films).element(2).hasFieldOrPropertyWithValue("id", 3L);
                });
    }

    @Test
    @Order(11)
    @DisplayName("FilmStorage - getById")
    void testFindFilmById() {
        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.getById(1L));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        {
                            assertThat(film).hasFieldOrPropertyWithValue("id", 1L);
                            assertThat(film.getMpa()).hasFieldOrPropertyWithValue("id", 2L);
                            assertThat(film.getGenres()).size().isEqualTo(1);
                            assertThat(film.getGenres()).hasSize(1);
                            assertThat(film.getGenres()).first().hasFieldOrPropertyWithValue("id", 1L);
                            assertThat(film.getLikes()).hasSize(2);
                            assertThat(film.getLikes()).first().isEqualTo(1L);
                        }
                );
    }

    @Test
    @Order(12)
    @DisplayName("FilmStorage - update")
    void testUpdate() {
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
    @Order(13)
    @DisplayName("FilmStorage - addLike")
    void testAddLike() {
        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.addLike(1L, 4L));
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

    @Test
    @Order(14)
    @DisplayName("FilmStorage - removeLike")
    void testRemoveLike() {
        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.removeLike(1L, 2L));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film -> {
                            assertThat(film.getLikes()).hasSize(2);
                            assertThat(film).hasFieldOrPropertyWithValue("id", 1L);
                            assertThat(film.getLikes()).element(0).isEqualTo(1L);
                            assertThat(film.getLikes()).element(1).isEqualTo(4L);
                        }
                );
    }

    //------------------------------------------------------------------------------------------------------------------
    //---FriendStorage--------------------------------------------------------------------------------------------------
    @Test
    @Order(15)
    @DisplayName("FriendStorage - getFriends by userId")
    void testGetFriends() {
        Optional<List<Friend>> friendsOptional = Optional.ofNullable(friendStorage.getFriends(1L));
        assertThat(friendsOptional)
                .isPresent()
                .hasValueSatisfying(friends -> {
                            assertThat(friends).isNotEmpty();
                            assertThat(friends).hasSize(1);
                            assertThat(friends).element(0).hasFieldOrPropertyWithValue("userId", 3L);
                        }
                );
    }

    //------------------------------------------------------------------------------------------------------------------
    //---GenreStorage---------------------------------------------------------------------------------------------------
    @Test
    @Order(16)
    @DisplayName("GenreStorage - getById")
    void testGenreById() {
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
    @Order(17)
    @DisplayName("GenreStorage - getAll")
    void testGenreGetAll() {
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

    //------------------------------------------------------------------------------------------------------------------
    //---LikeStorage----------------------------------------------------------------------------------------------------
    @Test
    @Order(18)
    @DisplayName("LikeStorage - getUsersLikeId by filmId")
    void testGetUsersIDLikesFilm() {
        Optional<List<Long>> userIdOptional = Optional.ofNullable(likeStorage.getUsersLikeId(1L));
        assertThat(userIdOptional)
                .isPresent()
                .hasValueSatisfying(users -> {
                            assertThat(users).isNotEmpty();
                            assertThat(users).hasSize(2);
                            assertThat(users).contains(1L, 4L);
                        }
                );
    }

    //------------------------------------------------------------------------------------------------------------------
    //---RatingMPAStorage-----------------------------------------------------------------------------------------------
    @Test
    @Order(19)
    @DisplayName("RatingMPAStorage - getById")
    void testMPAById() {
        Optional<RatingMPA> ratingMPAOptional = Optional.ofNullable(mpaStorage.getById(1L));
        assertThat(ratingMPAOptional)
                .isPresent()
                .hasValueSatisfying(mpa ->
                        {
                            assertThat(mpa).hasFieldOrPropertyWithValue("id", 1L);
                            assertThat(mpa).hasFieldOrPropertyWithValue("name", "G");
                        }
                );
    }

    @Test
    @Order(20)
    @DisplayName("RatingMPAStorage - getAll")
    void testGetAllRatingMPA() {
        Optional<Collection<RatingMPA>> ratingMPAOptional = Optional.ofNullable(mpaStorage.getAll());
        assertThat(ratingMPAOptional)
                .isPresent()
                .hasValueSatisfying(mpa ->
                {
                    assertThat(mpa).isNotEmpty();
                    assertThat(mpa).hasSize(5);
                    assertThat(mpa).element(0).hasFieldOrPropertyWithValue("id", 1L);
                    assertThat(mpa).element(0).hasFieldOrPropertyWithValue("name", "G");
                    assertThat(mpa).element(1).hasFieldOrPropertyWithValue("id", 2L);
                    assertThat(mpa).element(1).hasFieldOrPropertyWithValue("name", "PG");
                    assertThat(mpa).element(2).hasFieldOrPropertyWithValue("id", 3L);
                    assertThat(mpa).element(2).hasFieldOrPropertyWithValue("name", "PG-13");
                    assertThat(mpa).element(3).hasFieldOrPropertyWithValue("id", 4L);
                    assertThat(mpa).element(3).hasFieldOrPropertyWithValue("name", "R");
                    assertThat(mpa).element(4).hasFieldOrPropertyWithValue("id", 5L);
                    assertThat(mpa).element(4).hasFieldOrPropertyWithValue("name", "NC-17");
                });
    }
}
