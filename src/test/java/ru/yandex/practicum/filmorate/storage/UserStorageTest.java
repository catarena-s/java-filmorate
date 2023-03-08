package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserStorageTest {
    private final UserStorage userStorage;

    @Test
    @Order(1)
    @DisplayName("UserStorage - create")
    void createUser() {
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
                            assertThat(user).hasFieldOrPropertyWithValue("id", 5L);
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
    void getAllUsers() {
        Optional<Collection<User>> userList = Optional.ofNullable(userStorage.getAll());
        assertThat(userList)
                .isPresent()
                .hasValueSatisfying(user ->
                {
                    assertThat(user).isNotEmpty();
                    assertThat(user).hasSize(5);
                    assertThat(user).element(0).hasFieldOrPropertyWithValue("id", 1L);
                    assertThat(user).element(1).hasFieldOrPropertyWithValue("id", 2L);
                    assertThat(user).element(2).hasFieldOrPropertyWithValue("id", 3L);
                    assertThat(user).element(3).hasFieldOrPropertyWithValue("id", 4L);
                    assertThat(user).element(4).hasFieldOrPropertyWithValue("id", 5L);
                });
    }

    @Test
    @Order(3)
    @DisplayName("UserStorage - update")
    void updateUser() {
        User newUser = User.builder()
                .id(5)
                .login("test_user4")
                .name("New name User4")
                .birthday(LocalDate.of(2010, 10, 5))
                .email("testUser@email.com")
                .build();

        Optional<User> userOptional = Optional.ofNullable(userStorage.update(newUser));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user -> {
                            assertThat(user).hasFieldOrPropertyWithValue("id", 5L);
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
    void getUserFriends() {
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
    void getUserById() {
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
                            assertThat(user.getFriends()).hasSize(2);
                            assertThat(user.getFriends()).element(0).hasFieldOrPropertyWithValue("userId", 2L);
                            assertThat(user.getFriends()).element(1).hasFieldOrPropertyWithValue("userId", 3L);
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
    void testRemoveFromFriends() {
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

}