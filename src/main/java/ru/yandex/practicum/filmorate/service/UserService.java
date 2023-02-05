package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class UserService implements IService<User> {
    private final UserStorage storage;

    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    /**
     * Добавить друга
     *
     * @param userId   id пользователя
     * @param friendId id друга
     */
    public User addFriend(long userId, long friendId) {
        User user = storage.get(userId);
        User friend = storage.get(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
        return user;
    }

    /**
     * удалить пользователя из друзей
     *
     * @param userId   id пользователя
     * @param friendId id друга, которого нужно удалить
     */
    public User removeFromFriends(long userId, long friendId) {
        User user = storage.get(userId);
        User friend = storage.get(friendId);
        user.getFriends().remove(friend.getId());
        friend.getFriends().remove(user.getId());
        return user;
    }

    /**
     * Получить список общих друзей
     */
    public List<User> getCommonFriends(long userId, long otherId) {
        User user = storage.get(userId);
        User otherUser = storage.get(otherId);
        Set<Long> user1Friends = new HashSet<>(user.getFriends());
        Set<Long> user2Friends = new HashSet<>(otherUser.getFriends());
        user1Friends.retainAll(user2Friends);
        return storage.getUsers(user1Friends);
    }

    /**
     * Получить список друзей пользователя
     */
    public List<User> getFriends(long userId) {
        return storage.getFriends(userId);
    }

    @Override
    public User create(User obj) {
        return storage.create(obj);
    }

    @Override
    public Collection<User> getAll() {
        return storage.getAll();
    }

    @Override
    public User update(User obj) {
        return storage.update(obj);
    }

    @Override
    public User get(long id) {
        return storage.get(id);
    }
}
