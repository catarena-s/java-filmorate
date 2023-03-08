package ru.yandex.practicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.FilmGenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
@Slf4j
public class FilmGenreDBStorage extends DaoStorage implements FilmGenreStorage {

    protected FilmGenreDBStorage(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public Map<Long, Set<Long>> getSetGenresForTopFilms(int top) {
        final String sql = "SELECT DISTINCT fg.*  FROM film_genre AS fg  \n" +
                "RIGHT JOIN (\n" +
                "SELECT l.film_id\n" +
                "FROM likes AS l  \n" +
                "GROUP BY l.film_id \n" +
                "ORDER BY count(l.film_id) DESC, l.film_id ASC\n" +
                "LIMIT ? ) AS ll ON ll.film_id = fg.film_id \n";
        log.debug(LOG_MESSAGE_SQL_REQUEST, sqlQueryToString(sql, top));
        Map<Long, Set<Long>> setGenres = new HashMap<>();
        jdbcTemplate.query(sql,
                (rs, rowNum) -> mapper(rs, setGenres), top);
        return setGenres;
    }

    @Override
    public Map<Long, Set<Long>> getSetGenresFroAllFilms() {
        final String sql = "SELECT *  FROM film_genre";
        log.debug(LOG_MESSAGE_SQL_REQUEST, sqlQueryToString(sql));
        Map<Long, Set<Long>> setGenres = new HashMap<>();
        jdbcTemplate.query(sql, (rs, rowNum) -> mapper(rs, setGenres));
        return setGenres;
    }

    private Object mapper(ResultSet rs, Map<Long, Set<Long>> setGenres) throws SQLException {
        final long film_id = rs.getLong("film_id");
        Set<Long> list = setGenres.getOrDefault(film_id, new HashSet<>());
        list.add(rs.getLong("genre_id"));
        setGenres.put(film_id, list);
        return setGenres;
    }
}
