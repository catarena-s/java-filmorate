package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController extends Controller<Film> {
    protected FilmController() {
        setService(new FilmService());
    }
}
