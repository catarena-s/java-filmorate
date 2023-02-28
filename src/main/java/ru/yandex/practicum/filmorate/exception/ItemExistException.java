package ru.yandex.practicum.filmorate.exception;

import java.util.function.Consumer;

public class ItemExistException extends FilmorateException {
    public ItemExistException(String message, String description, Consumer<String> log) {
        super(message, description, log);
    }

    public ItemExistException(String message, Consumer<String> log) {
        super(message, log);
    }

    public ItemExistException(String message, String description) {
        super(message, description);
    }

    public ItemExistException(String message) {
        super(message);
    }
}
