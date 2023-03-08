package ru.yandex.practicum.filmorate.storage;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface LikeStorage {
    List<Long> getUsersLikeId(long filmId);
    boolean isNotExistLikeFromUSer(long filmId, long userId);
    Map<Long, Set<Long>> getSetLikesForTopFilms(int top);
    Map<Long, Set<Long>> getSetLikesForAllFilms();
}
