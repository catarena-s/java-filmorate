package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController extends Controller<Film> {
    protected FilmController() {
        setService(new FilmService());
    }

    @Override
    public Collection<Film> getAll() {
        log.info("Получен запрос GET /films");
        return super.getAll();
    }

    @Override
    public Film create(Film obj) {
        log.info("Получен запрос POST /films :");
        log.info("добавить : {}", obj);
        return super.create(obj);
    }

    @Override
    public Film update(Film obj) {
        log.info("Получен запрос PUT /films :");
        log.info("новое значение : {}", obj);
        return super.update(obj);
    }
}
