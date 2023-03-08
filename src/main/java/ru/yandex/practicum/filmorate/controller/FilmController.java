package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController extends Controller<Film> {

    @Autowired
    public FilmController(FilmService service) {
        super(service);
    }

    @Override
    public Collection<Film> getAll() {
        log.debug("Получен запрос GET '/films'");
        return super.getAll();
    }

    @Override
    public Film create(Film obj) {
        log.debug("Получен запрос POST '/films' :");
        log.debug("добавить : {}", obj);
        return super.create(obj);
    }

    @Override
    public Film update(Film obj) {
        log.debug("Получен запрос PUT '/films' :");
        log.debug("новое значение : {}", obj);
        return super.update(obj);
    }

    @Override
    public Film getById(@PathVariable(name = "id") long id) {
        log.debug("Получен запрос GET '/films/{}' :", id);
        return super.getById(id);
    }

    @PutMapping(value = "/{id}/like/{userId}")
    public Film putLike(@PathVariable(name = "id") long filmId,
                        @PathVariable(name = "userId") long userId) {
        log.debug("Получен PUT запрос '/films/{}/like/{}'", filmId, userId);
        return ((FilmService) service).addLike(filmId, userId);
    }

    @DeleteMapping(value = "/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Film removeLike(@PathVariable(name = "id") long filmId,
                           @PathVariable(name = "userId") long userId) {
        log.debug("Получен DELETE запрос '/films/{}/friends/{}'", filmId, userId);
        return ((FilmService) service).removeLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> getTopByLikes(
            @RequestParam(name = "count", value = "count", defaultValue = "10", required = false) int count) {
        log.debug("Получен запрос GET '/films/popular?count={}'", count);
        if (count < 0) {
            throw new IncorrectParameterException("count", count, log::error);
        }
        return ((FilmService) service).getTopByLikes(count);
    }
}
