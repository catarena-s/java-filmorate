package ru.yandex.practicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmorateException;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.util.List;

@Component()
@Primary
@Slf4j
public class LikeDbStorage extends DaoStorage implements LikeStorage {

    @Autowired
    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public List<Long> getUsersLikeId(long filmId) {
        final String sql = "SELECT user_id FROM likes WHERE film_id = ?";
        try {
            log.debug(LOG_MESSAGE_SQL_REQUEST, sqlQueryToString(sql, filmId));
            return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong("user_id"), filmId);
        } catch (Exception ex) {
            throw new FilmorateException(
                    MSG_ERROR_SQL_REQUEST + ", таблица likes",
                    sqlQueryToString(sql, filmId),
                    log::error);
        }
    }

    boolean isNotExistLikeFromUSer(long filmId, long userId) {
        final int count = getCount("SELECT COUNT(*) FROM likes WHERE film_id = ? AND user_id = ?", filmId, userId);
        return count == 0;
    }
}
