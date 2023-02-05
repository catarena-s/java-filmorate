package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import ru.yandex.practicum.filmorate.exception.ItemNotFoundException;
import ru.yandex.practicum.filmorate.model.FilmorateObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public abstract class AbstractStorage<T extends FilmorateObject> {
    protected final Map<Long, T> map = new HashMap<>();
    private int lastId = 0;
    private Logger log;
    private String storageType = "item";

    /**
     * Получить все объекты
     */
    public Collection<T> getAll() {
        return map.values();
    }

    /**
     * получить значение по id
     */
    public T get(long id) {
        if (!map.containsKey(id))
            throw new ItemNotFoundException(
                    String.format("%s с id=%d не найден", storageType, id)
                    , log);
        return map.get(id);
    }

    /**
     * Добавление нового объекта
     */
    public T create(T obj) {
        validate(obj);
        obj.setId(getNextId());
        map.put(obj.getId(), obj);
        return obj;
    }

    /**
     * Обновление
     */
    public T update(T obj) {
        validate(obj);
        if (map.containsKey(obj.getId())) {
            map.put(obj.getId(), obj);
        } else {
            throw new ItemNotFoundException(String.format("%s с id=%d не найден"
                    , storageType, obj.getId()), log);
        }
        return obj;
    }

    private int getNextId() {
        return ++lastId;
    }

    abstract void validate(T obj);
}
