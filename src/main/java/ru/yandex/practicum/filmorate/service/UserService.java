package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements IService<User> {
    private final UserStorage storage;

    /**
     * Добавить друга
     *
     * @param userId   id пользователя
     * @param friendId id друга
     */
    public User addFriend(long userId, long friendId) {
        return storage.addFriend(userId, friendId);
    }

    /**
     * удалить пользователя из друзей
     *
     * @param userId   id пользователя
     * @param friendId id друга, которого нужно удалить
     */
    public User removeFromFriends(long userId, long friendId) {
        return storage.removeFromFriends(userId, friendId);
    }

    /**
     * Получить список общих друзей
     */
    public List<User> getCommonFriends(long userId, long otherId) {
        return storage.getCommonFriends(userId, otherId);
    }

    /**
     * Получить список друзей пользователя
     */
    public List<User> getFriends(long userId) {
        return storage.getFriendsForUser(userId);
    }

    @Override
    public User create(User obj) {
        validate(obj);
        return storage.create(obj);
    }

    @Override
    public Collection<User> getAll() {
        return storage.getAll();
    }

    @Override
    public User update(User obj) {
        validate(obj);
        return storage.update(obj);
    }

    @Override
    public User getById(long id) {
        return storage.getById(id);
    }

    protected void validate(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.warn("Поле 'name' не заполнено. Было инициализировано значением login = '{}'.", user.getLogin());
        }
    }
}
