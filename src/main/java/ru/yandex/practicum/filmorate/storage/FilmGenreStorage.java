package ru.yandex.practicum.filmorate.storage;

import java.util.Map;
import java.util.Set;

public interface FilmGenreStorage {
    Map<Long, Set<Long>> getSetGenresForTopFilms(int top);
    Map<Long, Set<Long>> getSetGenresFroAllFilms();
}