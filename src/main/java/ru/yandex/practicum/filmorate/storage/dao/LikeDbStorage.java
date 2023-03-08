package ru.yandex.practicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmorateException;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component()
@Primary
@Slf4j
public class LikeDbStorage extends DaoStorage implements LikeStorage {

    @Autowired
    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public boolean isNotExistLikeFromUSer(long filmId, long userId) {
        final int count = getCount("SELECT COUNT(*) FROM likes WHERE film_id = ? AND user_id = ?", filmId, userId);
        return count == 0;
    }

    @Override
    public List<Long> getUsersLikeId(long filmId) {
        final String sql = "SELECT user_id FROM likes WHERE film_id = ?";
        try {
            log.debug(LOG_MESSAGE_SQL_REQUEST, sqlQueryToString(sql, filmId));
            return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong("user_id"), filmId);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new FilmorateException(
                    String.format("Ошибка при получении списка пользователей, поставивших лайк фильму(filmID= %d)", filmId),
                    log::error);
        }
    }

    @Override
    public Map<Long, Set<Long>> getSetLikesForTopFilms(int top) {
        final String sql = "\n" +
                "SELECT l2.* FROM likes AS l2 \n" +
                "RIGHT JOIN (\n" +
                "SELECT l.film_id \n" +
                "FROM likes AS l\n" +
                "GROUP BY l.film_id\n" +
                "ORDER BY COUNT(l.film_id) DESC, l.film_id ASC \n" +
                "LIMIT ? ) AS ll on l2.film_id =ll.film_id";
        log.debug(LOG_MESSAGE_SQL_REQUEST, sqlQueryToString(sql, top));
        Map<Long, Set<Long>> setLikes = new HashMap<>();
        jdbcTemplate.query(sql,
                (rs, rowNum) -> mapper(rs, setLikes), top);
        return setLikes;
    }

    @Override
    public Map<Long, Set<Long>> getSetLikesForAllFilms() {
        final String sql = "SELECT * FROM likes";
        log.debug(LOG_MESSAGE_SQL_REQUEST, sqlQueryToString(sql));
        Map<Long, Set<Long>> setLikes = new HashMap<>();
        jdbcTemplate.query(sql, (rs, rowNum) -> mapper(rs, setLikes));
        return setLikes;
    }

    private Object mapper(ResultSet rs, Map<Long, Set<Long>> setLikes) throws SQLException {
        final long film_id = rs.getLong("film_id");
        Set<Long> set = setLikes.getOrDefault(film_id, new HashSet<>());
        set.add(rs.getLong("user_id"));
        setLikes.put(film_id, set);
        return setLikes;
    }
}
