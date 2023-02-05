package ru.yandex.practicum.filmorate.storage;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Setter
@Slf4j
public class InMemoryFilmStorage extends AbstractStorage<Film> implements FilmStorage {
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
    void validate(Film obj) {
    }

    private int compare(Film f1, Film f2) {
        return f2.getLikes().size() - f1.getLikes().size();
    }
}
