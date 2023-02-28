package ru.yandex.practicum.filmorate.storage.dao;

import org.slf4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exception.ItemExistException;
import ru.yandex.practicum.filmorate.exception.ItemNotFoundException;

import java.util.function.Consumer;

public abstract class DaoStorage {
    protected static final String MSG_ITEM_NOT_FOUND = "%s с id=%d не найден";
    protected static final String LOG_MESSAGE_SQL_REQUEST = "SQL запрос: [{}]";
    protected static final String MSG_ERROR_INSERT_INTO = "Ошибка при добавлении данных в таблицу";
    protected static final String MGS_WAS_REMOVE_LINES_FROM_TABLE = "Было удалено {} строк из таблицы";
    protected static final String MSG_ERROR_SQL_REQUEST = "Ошибка выполнения sql запроса";
    protected static final String MSG_ERROR_SQL_QUERY = "Ошибка выполнения запроса: %s";
    protected final JdbcTemplate jdbcTemplate;

    protected DaoStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public static String sqlQueryToString(String sql, Object... args) {
        return String.format(sql.replace("?", "%s"), args);
    }

    protected void throwExceptionIfNoExistById(String sql, long id, String objName, Logger log) {
        if (getCount(sql, id) == 0) {
            throw new ItemNotFoundException(
                    String.format(MSG_ITEM_NOT_FOUND, objName, id),
                    log::error);
        }
    }

    protected void throwErrorIfExist(String sql, String itemName, String paramName, Object value, Consumer<String> log) {
        if (getCount(sql, value) != 0) {
            throw new ItemExistException(
                    String.format("%s с %s = %s уже существует", itemName, paramName, value),
                    sqlQueryToString(sql, value),
                    log);
        }
    }

    protected Integer getCount(String sql, Object... values) {
        return jdbcTemplate.queryForObject(sql, Integer.class, values);
    }
}
