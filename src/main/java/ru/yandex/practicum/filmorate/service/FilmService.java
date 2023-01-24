package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Film;

@Slf4j
public class FilmService extends Service<Film> {
    public FilmService() {
        super();
        setLogger(log);
        setType("/films");
    }
}
