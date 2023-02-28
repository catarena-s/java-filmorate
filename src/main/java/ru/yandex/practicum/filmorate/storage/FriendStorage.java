package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Friend;

import java.util.List;

public interface FriendStorage {
    List<Friend> getFriends(long userId);
}
