package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage extends Storage<Film> {
    List<Film> getTopByLikes(int top);

    Film addLike(long filmId, long userId);

    Film removeLike(long filmId, long userId);
}
