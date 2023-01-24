package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.FilmorateObject;
import ru.yandex.practicum.filmorate.service.Service;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
    public T add(@Valid @RequestBody T obj) {
        return service.add(obj);
    }

    @PutMapping
    @Override
    public T update(@Valid @RequestBody T obj) {
        return service.update(obj);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        String format = "'%s' -> %s";
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage =String.format (format,((FieldError) error).getRejectedValue() , error.getDefaultMessage() );
            errors.put(fieldName, errorMessage);
        });
        service.getLogger().error(errors.toString());
        return errors;
    }
}
