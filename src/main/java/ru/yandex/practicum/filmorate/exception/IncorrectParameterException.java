package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;

import java.util.function.Consumer;

@Getter
public class IncorrectParameterException extends FilmorateException {
    protected static final String INCORRECT_PARAM_MSG = "Ошибка с полем %s = %s";
    private final String parameter;
    private final Object value;

    public IncorrectParameterException(String parameter, Object value, Consumer<String> log) {
        super(String.format(INCORRECT_PARAM_MSG, parameter, value), log);
        this.parameter = parameter;
        this.value = value;
    }

    public IncorrectParameterException(String parameter, Object value) {
        super(String.format(INCORRECT_PARAM_MSG, parameter, value));
        this.parameter = parameter;
        this.value = value;
    }
}
