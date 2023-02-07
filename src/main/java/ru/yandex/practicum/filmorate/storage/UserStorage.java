package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface UserStorage extends Storage<User> {
    List<User> getUsers(Set<Long> set);
    List<User> getFriends(long userId);
}
