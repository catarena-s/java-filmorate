package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.RatingMPA;

import java.util.Collection;

public interface RatingMPAStorage {
    RatingMPA getById(long id);

    Collection<RatingMPA> getAll();
}
