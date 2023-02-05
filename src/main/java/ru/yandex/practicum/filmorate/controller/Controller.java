package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.FilmorateObject;
import ru.yandex.practicum.filmorate.service.IService;

import javax.validation.Valid;
import java.util.Collection;

@Setter
@Getter
public abstract class Controller<T extends FilmorateObject> implements IController<T> {
    final IService<T> service;

    protected Controller(IService<T> service) {
        this.service = service;
    }

    @GetMapping
    @Override
    public Collection<T> getAll() {
        return service.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Override
    public T create(@Valid @RequestBody T obj) {
        return service.create(obj);
    }

    @PutMapping
    @Override
    public T update(@Valid @RequestBody T obj) {
        return service.update(obj);
    }

    @GetMapping("/{id}")
    @Override
    public T get(long id) {
        return service.get(id);
    }
}
