package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.RatingMPA;
import ru.yandex.practicum.filmorate.service.RatingMPAService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class RatingMPAController {
    private final RatingMPAService service;

    @GetMapping
    public Collection<RatingMPA> getAll() {
        log.debug("Получен запрос GET '/mpa'");
        return service.getAll();
    }

    @GetMapping("/{id}")
    public RatingMPA getById(@PathVariable(name = "id") int id) {
        log.debug("Получен запрос GET '/mpa/{}' :", id);
        return service.getById(id);
    }
}
