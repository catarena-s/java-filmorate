package ru.yandex.practicum.filmorate.storage;

import java.util.List;

public interface LikeStorage {
    List<Long> getUsersLikeId(long filmId);
}
