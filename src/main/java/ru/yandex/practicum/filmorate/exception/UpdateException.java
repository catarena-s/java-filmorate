package ru.yandex.practicum.filmorate.exception;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Object not found")
public class UpdateException extends FilmorateException {

    public UpdateException(String message, Logger log) {
        super(message, log);
    }
}
