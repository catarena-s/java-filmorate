package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;

import java.util.function.Consumer;

@Getter
public class FilmorateException extends RuntimeException {
    private String description;

    public FilmorateException(String message, String description, Consumer<String> log) {
        super(message);
        this.description = description;
        log.accept("msg: ".concat(message));
        log.accept("description: ".concat(description));
    }

    public FilmorateException(String message, Consumer<String> log) {
        super(message);
        log.accept("msg: ".concat(message));
    }

    public FilmorateException(String message, String description) {
        super(message);
        this.description = description;
    }

    public FilmorateException(String message) {
        super(message);
    }
}
