package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Friend;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface FriendStorage {
    List<Friend> getFriends(long userId);
    Map<Long, Set<Friend>> getSetFriendsForCommonFriends(long userId, long otherId);
    Map<Long, Set<Friend>> getSetFriendsForAllUsers();
    Map<Long, Set<Friend>> getSetFriendsForUsers(long userId);
    void addFriendship(long userId, long friendId);
    void removeFromFriendship(long userId, long friendId);
    boolean isFriendshipExist(long friendId, long userId);
}
