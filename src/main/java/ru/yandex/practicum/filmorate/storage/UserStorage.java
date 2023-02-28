package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage extends Storage<User> {
    List<User> getFriendsForUser(long userId);

    List<User> getCommonFriends(long userId, long otherId);

    User removeFromFriends(long userId, long friendId);

    User addFriend(long userId, long friendId);
}
