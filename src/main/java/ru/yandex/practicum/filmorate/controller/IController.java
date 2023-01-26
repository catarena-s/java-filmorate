package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.Collection;

public interface IController<T> {
    Collection<T> getAll();
    T update(@Valid @RequestBody T obj);
    T create(@Valid @RequestBody T obj);
}
