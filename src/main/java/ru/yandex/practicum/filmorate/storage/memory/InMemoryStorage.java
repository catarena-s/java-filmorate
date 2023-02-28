package ru.yandex.practicum.filmorate.storage.memory;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import ru.yandex.practicum.filmorate.exception.ItemNotFoundException;
import ru.yandex.practicum.filmorate.model.FilmorateObject;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public abstract class InMemoryStorage<T extends FilmorateObject> implements Storage<T> {
    public static final String MSG_ITEM_NOT_FOUND = "%s с id=%d не найден";
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
                    String.format(MSG_ITEM_NOT_FOUND, storageType, id),
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
        checkExistItem(obj);
        return map.get(obj.getId());
    }

    protected void checkExistItem(T obj) {
        if (!map.containsKey(obj.getId())) {
            throw new ItemNotFoundException(
                    String.format(MSG_ITEM_NOT_FOUND, storageType, obj.getId()),
                    log::error);
        }
    }

    private int getNextId() {
        return ++lastId;
    }

}
