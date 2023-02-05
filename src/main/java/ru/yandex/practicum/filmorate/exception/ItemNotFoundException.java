package ru.yandex.practicum.filmorate.exception;

import org.slf4j.Logger;

public class ItemNotFoundException extends FilmorateException {

    public ItemNotFoundException(String message, Logger log) {
        super(message, log);
    }
}
