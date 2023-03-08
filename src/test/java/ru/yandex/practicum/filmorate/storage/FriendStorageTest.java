package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Friend;

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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FriendStorageTest {
    private final FriendStorage friendStorage;

    @Test
    @Order(1)
    @DisplayName("FriendStorage - getFriends by userId")
    void getFriends() {
        Optional<List<Friend>> friendsOptional = Optional.ofNullable(friendStorage.getFriends(1L));
        assertThat(friendsOptional)
                .isPresent()
                .hasValueSatisfying(friends -> {
                            assertThat(friends).isNotEmpty();
                            assertThat(friends).hasSize(2);
                            assertThat(friends).element(0).hasFieldOrPropertyWithValue("userId", 2L);
                            assertThat(friends).element(1).hasFieldOrPropertyWithValue("userId", 3L);
                        }
                );
    }

    @Test
    @Order(2)
    @DisplayName("FriendStorage - check is friendship exists for two users")
    void isFriendshipExist() {
        assertTrue(friendStorage.isFriendshipExist(1L, 2L));
        assertTrue(friendStorage.isFriendshipExist(1L, 3L));
        assertFalse(friendStorage.isFriendshipExist(1L, 4L));
    }

    @Test
    @Order(3)
    @DisplayName("FriendStorage - add friendship")
    void addFriendship() {
        friendStorage.addFriendship(4L, 3L);
        assertTrue(friendStorage.isFriendshipExist(4L, 3L));
    }

    @Test
    @Order(4)
    @DisplayName("FriendStorage - getSetFriends for each common friend")
    void getSetFriendsForCommonFriends() {
        Optional<Map<Long, Set<Friend>>> map = Optional.ofNullable(friendStorage.getSetFriendsForCommonFriends(1L, 4L));
        assertThat(map)
                .isPresent()
                .hasValueSatisfying(setMap -> {
                            assertThat(setMap).hasSize(2);
                            assertThat(setMap).containsKeys(2L, 3L);
                            assertThat(setMap.get(2L)).hasSize(1);
                            assertThat(setMap.get(3L)).hasSize(1);
                        }
                );
    }

    @Test
    @Order(5)
    @DisplayName("FriendStorage - getSetFriends for each user")
    void getSetFriendsForAllUsers() {
        Optional<Map<Long, Set<Friend>>> map = Optional.ofNullable(friendStorage.getSetFriendsForAllUsers());
        assertThat(map)
                .isPresent()
                .hasValueSatisfying(setMap -> {
                            assertThat(setMap).hasSize(4);
                            assertThat(setMap.get(1L)).hasSize(2);
                            assertThat(setMap.get(2L)).hasSize(1);
                            assertThat(setMap.get(3L)).hasSize(1);
                            assertThat(setMap.get(4L)).hasSize(3);
                        }
                );
    }

    @Test
    @Order(6)
    @DisplayName("FriendStorage - getSetFriends for user by id")
    void getSetFriendsForUsers() {
        Optional<Map<Long, Set<Friend>>> map = Optional.ofNullable(friendStorage.getSetFriendsForUsers(1L));
        assertThat(map)
                .isPresent()
                .hasValueSatisfying(setMap -> {
                            assertThat(setMap).hasSize(2);
                            assertThat(setMap).containsKeys(2L, 3L);
                            assertThat(setMap.get(2L)).hasSize(1);
                            assertThat(setMap.get(3L)).hasSize(1);
                        }
                );
    }

    @Test
    @Order(7)
    @DisplayName("FriendStorage - remove from friendship")
    void removeFromFriendship() {
        friendStorage.removeFromFriendship(4L, 3L);
        assertFalse(friendStorage.isFriendshipExist(4L, 3L));
    }
}