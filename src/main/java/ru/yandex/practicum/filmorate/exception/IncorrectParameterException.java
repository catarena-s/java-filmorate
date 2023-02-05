package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;
import org.slf4j.Logger;

@Getter
public class IncorrectParameterException extends FilmorateException {
    private final String parameter;
    private final Object value;
    private final Logger log;

    public IncorrectParameterException(Logger log, String parameter, Object value) {
        super(String.format("Ошибка с полем %s = %s", parameter, value), log);
        this.parameter = parameter;
        this.value = value;
        this.log = log;
    }
}
