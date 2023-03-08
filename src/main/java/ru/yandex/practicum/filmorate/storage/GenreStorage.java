package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;

public interface GenreStorage {
    Genre getById(long id);

    Collection<Genre> getAll();
    List<Genre> getByFilmId(long filmId);
}
