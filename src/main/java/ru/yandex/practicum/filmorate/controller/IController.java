package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.FilmorateObject;

import javax.validation.Valid;
import java.util.Collection;

public interface IController <T>{
    Collection<T> getAll();
    T update(@Valid @RequestBody T obj);
    T add(@Valid @RequestBody T obj);
}
