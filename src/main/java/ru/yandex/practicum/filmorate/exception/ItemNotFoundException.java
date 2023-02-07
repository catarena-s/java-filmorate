package ru.yandex.practicum.filmorate.exception;

import java.util.function.Consumer;

public class ItemNotFoundException extends FilmorateException {

    public ItemNotFoundException(String message, Consumer<String> log) {
        super(message, log);
    }

    public ItemNotFoundException(String message) {
        super(message);
    }
}
