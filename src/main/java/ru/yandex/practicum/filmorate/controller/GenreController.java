package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {
    final GenreService service;

    @GetMapping
    public Collection<Genre> getAll() {
        log.debug("Получен запрос GET '/genres'");
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Genre getById(@PathVariable(name = "id") int id) {
        log.debug("Получен запрос GET '/genres/{}' :", id);
        return service.getById(id);
    }
}
