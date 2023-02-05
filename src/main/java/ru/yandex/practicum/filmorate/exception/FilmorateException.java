package ru.yandex.practicum.filmorate.exception;

import org.slf4j.Logger;

public class FilmorateException extends RuntimeException {
    public FilmorateException(String message, Logger log) {
        super(message);
        log.error(message);
    }
}
