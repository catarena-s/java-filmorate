package ru.yandex.practicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ItemNotFoundException;
import ru.yandex.practicum.filmorate.model.RatingMPA;
import ru.yandex.practicum.filmorate.storage.RatingMPAStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Component()
@Slf4j
public class RatingMPADbStorage extends DaoStorage implements RatingMPAStorage {

    @Autowired
    public RatingMPADbStorage(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public RatingMPA getById(long id) {
        try {
            final String sql = "SELECT * FROM rating WHERE rating_id = ? ORDER BY rating_id";
            log.debug(LOG_MESSAGE_SQL_REQUEST, sqlQueryToString(sql, id));
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeRating(rs), id);
        } catch (Exception e) {
            throw new ItemNotFoundException(String.format("Rating MPA не найден id=%d", id), log::error);
        }
    }


    @Override
    public Collection<RatingMPA> getAll() {
        final String sql = "SELECT * FROM rating ORDER BY rating_id";
        log.debug(LOG_MESSAGE_SQL_REQUEST, sql);
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeRating(rs));
    }

    private RatingMPA makeRating(ResultSet rs) throws SQLException {
        return RatingMPA.builder()
                .id(rs.getInt("rating_id"))
                .name(rs.getString("name"))
                .build();
    }
}
