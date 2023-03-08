package ru.yandex.practicum.filmorate.exception;

import java.util.function.Consumer;

public class ValidationException extends FilmorateException {
    public ValidationException(String message, Consumer<String> log) {
        super(message, log);
    }

    public ValidationException(String message, String description) {
        super(message, description);
    }

    public ValidationException(String message, String description, Consumer<String> log) {
        super(message, description, log);
    }

    public ValidationException(String message) {
        super(message);
    }
}
