package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.FilmorateObject;

import java.util.Collection;

public interface Storage<T extends FilmorateObject> {
    T create(T obj);

    Collection<T> getAll();

    T update(T obj);

    T getById(long id);
}
