package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.FilmorateObject;
import ru.yandex.practicum.filmorate.service.Service;

import javax.validation.Valid;
import java.util.Collection;

@Setter
@Getter
public abstract class Controller<T extends FilmorateObject> implements IController<T> {
    private Service<T> service;

    @GetMapping
    @Override
    public Collection<T> getAll() {
        return service.getAll();
    }

    @PostMapping
    @Override
    public T create(@Valid @RequestBody T obj) {
        return service.create(obj);
    }

    @PutMapping
    @Override
    public T update(@Valid @RequestBody T obj) {
        return service.update(obj);
    }
}
