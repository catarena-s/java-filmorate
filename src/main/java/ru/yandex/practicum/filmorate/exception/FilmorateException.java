package ru.yandex.practicum.filmorate.exception;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Bad request")
public class FilmorateException extends RuntimeException {
    public FilmorateException(String message, Logger log) {
        super(message);
        log.error(message);
    }
}
