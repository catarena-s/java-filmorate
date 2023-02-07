package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;

import java.util.function.Consumer;

@Getter
public class IncorrectParameterException extends FilmorateException {
    private final String parameter;
    private final Object value;

    public IncorrectParameterException(String parameter, Object value, Consumer<String> log) {
        super(String.format("Ошибка с полем %s = %s", parameter, value), log);
        this.parameter = parameter;
        this.value = value;
    }

    public IncorrectParameterException(String message, String parameter, Object value) {
        super(message);
        this.parameter = parameter;
        this.value = value;
    }
}
