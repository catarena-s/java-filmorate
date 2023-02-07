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
public abstract class AbstractStorage<T extends FilmorateObject> implements Storage<T> {
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
    @Override
    public T getById(long id) {
        if (!map.containsKey(id))
            throw new ItemNotFoundException(
                    String.format("%s с id=%d не найден", storageType, id),
                    log::error);
        return map.get(id);
    }

    /**
     * Добавление нового объекта
     */
    @Override
    public T create(T obj) {
        obj.setId(getNextId());
        map.put(obj.getId(), obj);
        return obj;
    }

    /**
     * Обновление
     */
    @Override
    public T update(T obj) {
        if (map.containsKey(obj.getId())) {
            map.put(obj.getId(), obj);
        } else {
            throw new ItemNotFoundException(
                    String.format("%s с id=%d не найден", storageType, obj.getId()),
                    log::error);
        }
        return obj;
    }

    private int getNextId() {
        return ++lastId;
    }

}
