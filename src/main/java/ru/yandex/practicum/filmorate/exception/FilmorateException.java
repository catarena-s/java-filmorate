package ru.yandex.practicum.filmorate.exception;

import java.util.function.Consumer;

public class FilmorateException extends RuntimeException {
    public FilmorateException(String message, Consumer<String> log) {
        super(message);
        log.accept(message);
    }

    public FilmorateException(String message) {
        super(message);
    }
}
