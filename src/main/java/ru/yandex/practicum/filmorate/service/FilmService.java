package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService implements IService<Film> {

    private final FilmStorage storage;

    /**
     * Добавить лайк фильму
     *
     * @param filmId id фильма
     * @param userId id пользователя
     * @return обновленный фильм
     */
    public Film addLike(long filmId, long userId) {
        return storage.addLike(filmId, userId);
    }

    /**
     * Удалить лайк пользователя у фильма
     *
     * @param filmId фильм
     * @param userId пользователь
     * @return обновленный фильм
     */
    public Film removeLike(long filmId, long userId) {
        return storage.removeLike(filmId, userId);
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
    public Film getById(long id) {
        return storage.getById(id);
    }

    private void validate(Film obj) {
        //дополнительная валидация, если не удалось реализовать через аннотации
    }
}
