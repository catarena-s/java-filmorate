package ru.yandex.practicum.filmorate.storage.memory;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ItemNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Setter
@Slf4j
public class InMemoryFilmStorage extends InMemoryStorage<Film> implements FilmStorage {
    public InMemoryFilmStorage() {
        super();
        setLog(log);
        setStorageType("Film");
    }

    /**
     * Получить список отсортированный по убыванию количества лайков
     *
     * @param countTop количество фильмов в списке
     */
    @Override
    public List<Film> getTopByLikes(int countTop) {
        return map.values().stream()
                .sorted(this::compare)
                .limit(countTop)
                .collect(Collectors.toList());
    }

    @Override
    public Film addLike(long filmId, long userId) {
        Film film = getById(filmId);
        film.addLike(userId);
        return film;
    }

    @Override
    public Film removeLike(long filmId, long userId) {
        Film film = getById(filmId);
        if (!film.getLikes().contains(userId))
            throw new ItemNotFoundException(
                    String.format("Likes от пользователя id=%d не не найдены", userId),
                    log::error);
        film.getLikes().remove(userId);
        return film;
    }

    @Override
    public Film update(Film obj) {
        checkExistItem(obj);
        final Film film = map.get(obj.getId());
        return film.updateData(obj);
    }

    private int compare(Film f1, Film f2) {
        return f2.getCountLikes() - f1.getCountLikes();
    }
}
