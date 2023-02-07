package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ItemNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
public class FilmService implements IService<Film> {
    private final FilmStorage storage;

    @Autowired
    public FilmService(FilmStorage storage) {
        this.storage = storage;
    }

    /**
     * Добавить лайк фильму
     *
     * @param filmId id фильма
     * @param userId id пользователя
     * @return обновленный фильм
     */
    public Film addLike(long filmId, long userId) {
        Film film = storage.getById(filmId);
        film.getLikes().add(userId);
        return film;
    }

    /**
     * Удалить лайк пользователя у фильма
     *
     * @param filmId фильм
     * @param userId пользователь
     * @return обновленный фильм
     */
    public Film removeLike(long filmId, long userId) {
        Film film = storage.getById(filmId);
        if (!film.getLikes().contains(userId))
            throw new ItemNotFoundException(
                    String.format("Likes от пользователя id=%d не не найдены", userId),
                    log::error);
        film.getLikes().remove(userId);
        return film;
    }

    /**
     * ТОП фильмов отсортированных по убыванию количества лайков
     *
     * @param count количество возвращаемых фильмов(ао умолчанию 10)
     * @return список фильмов
     */
    public List<Film> getTopByLikes(int count) {
        return storage.getTopByLikes(count);
    }

    @Override
    public Film create(Film obj) {
        validate(obj);
        return storage.create(obj);
    }

    @Override
    public Collection<Film> getAll() {
        return storage.getAll();
    }

    @Override
    public Film update(Film obj) {
        validate(obj);
        return storage.update(obj);
    }

    @Override
    public Film get(long id) {
        return storage.getById(id);
    }

    private void validate(Film obj) {
        //дополнительная валидация, если не удалось реализовать через аннотации
    }
}
