package ru.yandex.practicum.filmorate.storage.memory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryUserStorage extends InMemoryStorage<User> implements UserStorage {

    public InMemoryUserStorage() {
        super();
        setLog(log);
        setStorageType("User");
    }

    /**
     * Получить список пользователей по набору id
     *
     * @param set список id пользователей
     */
    public List<User> getUsers(Set<Long> set) {
        return map.entrySet().stream()
                .filter(x -> set.contains(x.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    /**
     * Получить друзей пользователя
     *
     * @param userId - id пользователя
     * @return - список друзей
     */
    @Override
    public List<User> getFriendsForUser(long userId) {
        Set<Long> friends = getFriendsIdCollection(map.get(userId));
        return map.entrySet().stream()
                .filter(m -> friends.contains(m.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getCommonFriends(long userId, long otherId) {
        User user = getById(userId);
        User otherUser = getById(otherId);
        Set<Long> user1Friends = new HashSet<>(getFriendsIdCollection(user));
        Set<Long> user2Friends = new HashSet<>(getFriendsIdCollection(otherUser));
        user1Friends.retainAll(user2Friends);
        return getUsers(user1Friends);
    }

    private Set<Long> getFriendsIdCollection(User otherUser) {
        return otherUser.getFriends().stream().map(Friend::getUserId).collect(Collectors.toSet());
    }

    @Override
    public User removeFromFriends(long userId, long friendId) {
        User user = getById(userId);
        User friend = getById(friendId);
        user.removeFriendById(friendId);
        friend.removeFriendById(user.getId());
        return user;
    }

    @Override
    public User addFriend(long userId, long friendId) {
        User user = getById(userId);
        User friend = getById(friendId);
        user.addFriend(friendId);
        friend.addFriend(userId);
        return user;
    }

    @Override
    public User update(User obj) {
        checkExistItem(obj);
        final User user = map.get(obj.getId());
        return user.updateData(obj);
    }

}
