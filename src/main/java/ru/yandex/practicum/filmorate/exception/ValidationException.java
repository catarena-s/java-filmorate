package ru.yandex.practicum.filmorate.exception;

import org.slf4j.Logger;

public class ValidationException extends FilmorateException {
    public ValidationException(String message, Logger log) {
        super(message, log);
    }
}
