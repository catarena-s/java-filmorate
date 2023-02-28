package ru.yandex.practicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ItemNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component()
@Slf4j
public class GenreDbStorage extends DaoStorage implements GenreStorage {

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public Genre getById(long id) {
        final String sql = "SELECT * FROM genre WHERE genre_id = ? ORDER BY genre_id";
        try {
            log.debug(LOG_MESSAGE_SQL_REQUEST, sqlQueryToString(sql, id));
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeGenre(rs), id);
        } catch (EmptyResultDataAccessException ex) {
            throw new ItemNotFoundException(String.format(MSG_ITEM_NOT_FOUND, "Genre", id),
                    ex.getMessage(),
                    log::error);
        }
    }

    @Override
    public Collection<Genre> getAll() {
        final String sql = "SELECT * FROM genre ORDER BY genre_id";
        log.debug(LOG_MESSAGE_SQL_REQUEST, sql);
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs));
    }

    public List<Genre> getByFilmId(long filmId) {
        final String sql = "SELECT genre_id FROM film_genre WHERE film_id = ? ORDER BY genre_id";
        log.debug(LOG_MESSAGE_SQL_REQUEST, sqlQueryToString(sql, filmId));
        final List<Integer> genreListId = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("genre_id"), filmId);
        return genreListId.stream()
                .map(this::getById)
                .collect(Collectors.toList());
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        return Genre.builder()
                .id(rs.getInt("genre_id"))
                .name(rs.getString("name"))
                .build();
    }
}
