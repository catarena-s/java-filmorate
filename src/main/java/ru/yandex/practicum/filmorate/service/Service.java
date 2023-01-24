package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import ru.yandex.practicum.filmorate.exception.UpdateException;
import ru.yandex.practicum.filmorate.model.FilmorateObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public abstract class Service<T extends FilmorateObject> {
    private int lastId;
    private String type;
    private Logger logger;
    private final Map<Integer, T> map = new HashMap<>();

    protected Service() {
        setLastId(0);
    }

    public T create(T obj) {
        logger.info("Получен запрос POST :");
        logger.info("добавить : {}", obj);
        validate(obj);
        obj.setId(++lastId);
        map.put(obj.getId(), obj);
        return obj;
    }

    public T update(T obj) {
        logger.info("Получен запрос PUT :");
        logger.info("новое значение : {}", obj);
        if (map.containsKey(obj.getId())) {
            map.put(obj.getId(), obj);
        } else {
            throw new UpdateException(String.format("%s с id=%d не найден"
                    , obj.getClass().getSimpleName(), obj.getId()), logger);
        }
        return obj;
    }

    public Collection<T> getAll() {
        logger.info("Получен запрос GET {}.", type);
        return map.values();
    }

    protected void validate(T obj) {
    }
}
