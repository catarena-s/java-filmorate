package ru.yandex.practicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmorateException;
import ru.yandex.practicum.filmorate.exception.ItemNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static ru.yandex.practicum.filmorate.storage.dao.DaoQueries.*;

@Component
@Primary
@Slf4j
public class FilmDbStorage extends DaoStorage implements FilmStorage {
    private final RatingMPADbStorage ratingMPADbStorage;
    private final GenreDbStorage genreDbStorage;
    private final LikeDbStorage filmLikeDbStorage;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate,
                         RatingMPADbStorage ratingMPADbStorage,
                         GenreDbStorage genreDbStorage,
                         LikeDbStorage filmLikeDbStorage) {
        super(jdbcTemplate);
        this.ratingMPADbStorage = ratingMPADbStorage;
        this.genreDbStorage = genreDbStorage;
        this.filmLikeDbStorage = filmLikeDbStorage;
    }

    @Override
    public Collection<Film> getAll() {
        log.debug(LOG_MESSAGE_SQL_REQUEST, SELECT_FROM_FILM);
        return jdbcTemplate.query(SELECT_FROM_FILM, (rs, rowNum) -> makeFilm(rs));
    }

    @Override
    public Film getById(long id) {
        try {
            log.debug(LOG_MESSAGE_SQL_REQUEST, sqlQueryToString(SELECT_FROM_FILM_WHERE_FILM_ID, id));
            return jdbcTemplate.queryForObject(SELECT_FROM_FILM_WHERE_FILM_ID, (rs, rowNum) -> makeFilm(rs), id);
        } catch (EmptyResultDataAccessException ex) {
            throw new ItemNotFoundException(String.format(MSG_ITEM_NOT_FOUND, "Film", id), log::error);
        }
    }

    @Override
    public Film create(Film film) {
        try {
            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("film")
                    .usingGeneratedKeyColumns("film_id");

            final long filmId = simpleJdbcInsert.executeAndReturnKey(film.toMap()).longValue();

            log.debug(LOG_MESSAGE_SQL_REQUEST, sqlQueryToString(DELETE_FROM_FILM_GENRE_WHERE_FILM_ID, filmId));
            jdbcTemplate.update(DELETE_FROM_FILM_GENRE_WHERE_FILM_ID, filmId);

            if (film.getGenres() != null && !film.getGenres().isEmpty()) {
                for (Genre genre : film.getGenres()) {
                    log.debug(LOG_MESSAGE_SQL_REQUEST, sqlQueryToString(INSERT_INTO_FILM_GENRE, filmId, genre.getId()));
                    jdbcTemplate.update(INSERT_INTO_FILM_GENRE, filmId, genre.getId());
                    film.addGenre(genreDbStorage.getById(genre.getId()));
                }
            }
            return getById(filmId);
        } catch (Exception ex) {
            throw new FilmorateException(ex.getMessage(), log::error);
        }
    }

    @Override
    public List<Film> getTopByLikes(int top) {
        final String sql = "SELECT f.*, COUNT(l.film_id) count_likes \n" +
                "FROM FILM f LEFT JOIN likes l ON f.FILM_ID = l.FILM_ID \n" +
                "GROUP BY f.film_id ORDER BY count(l.film_id) DESC, f.film_id ASC " +
                "LIMIT ?";
        try {
            log.debug(LOG_MESSAGE_SQL_REQUEST, sqlQueryToString(sql, top));
            return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), top);
        } catch (Exception ex) {
            throw new FilmorateException(
                    String.format(MSG_ERROR_SQL_QUERY, sql),
                    ex.getMessage(),
                    log::error);
        }
    }

    @Override
    public Film addLike(long filmId, long userId) {
        // проверяем наличие фильма
        throwExceptionIfNoExistById(SELECT_COUNT_FROM_FILM_WHERE_FILM_ID, filmId, "Film", log);
        // проверяем наличие пользователя
        throwExceptionIfNoExistById(SELECT_COUNT_FROM_USER_INFO_WHERE_USER_ID, userId, "User", log);
        // проверяем ставил ли пользователь лайк фильму
        if (filmLikeDbStorage.isNotExistLikeFromUSer(filmId, userId)) {
            try {
                log.debug(LOG_MESSAGE_SQL_REQUEST, sqlQueryToString(INSERT_INTO_LIKES, filmId, userId));
                jdbcTemplate.update(INSERT_INTO_LIKES, filmId, userId);
                return getById(filmId);
            } catch (Exception ex) {
                throw new FilmorateException(MSG_ERROR_INSERT_INTO + " likes", log::error);
            }
        }
        log.warn("User id={} уже ставил лайк фильму id = {}", userId, filmId);
        return getById(filmId);
    }

    @Override
    public Film removeLike(long filmId, long userId) {
        // проверяем наличие фильма
        throwExceptionIfNoExistById(SELECT_COUNT_FROM_FILM_WHERE_FILM_ID, filmId, "Film", log);
        // проверяем наличие пользователя
        throwExceptionIfNoExistById(SELECT_COUNT_FROM_USER_INFO_WHERE_USER_ID, userId, "User", log);

        log.debug(LOG_MESSAGE_SQL_REQUEST, sqlQueryToString(DELETE_FROM_LIKES, filmId, userId));
        final int count = jdbcTemplate.update(DELETE_FROM_LIKES, filmId, userId);
        log.debug(MGS_WAS_REMOVE_LINES_FROM_TABLE + " likes", count);
        return getById(filmId);
    }

    @Override
    public Film update(Film film) {
        //проверяем наличие фильма по id
        throwExceptionIfNoExistById(SELECT_COUNT_FROM_FILM_WHERE_FILM_ID, film.getId(), "Film", log);
        try {
            log.debug(LOG_MESSAGE_SQL_REQUEST,
                    sqlQueryToString(UPDATE_FILM, film.getName(), film.getDescription(),
                                    film.getDuration(), film.getReleaseDate(), film.getMpa().getId(), film.getId()));

            jdbcTemplate.update(UPDATE_FILM, film.getName(), film.getDescription(),
                    film.getDuration(), film.getReleaseDate(), film.getMpa().getId(), film.getId());

            log.debug(LOG_MESSAGE_SQL_REQUEST, sqlQueryToString(DELETE_FROM_FILM_GENRE_WHERE_FILM_ID, film.getId()));
            jdbcTemplate.update(DELETE_FROM_FILM_GENRE_WHERE_FILM_ID, film.getId());

            if (film.getGenres() != null && !film.getGenres().isEmpty()) {
                for (Genre genre : film.getGenres()) {
                    log.debug(LOG_MESSAGE_SQL_REQUEST, sqlQueryToString(INSERT_INTO_FILM_GENRE, film.getId(), genre.getId()));
                    jdbcTemplate.update(INSERT_INTO_FILM_GENRE, film.getId(), genre.getId());
                }
            }
            return getById(film.getId());
        } catch (Exception ex) {
            throw new FilmorateException(MSG_ERROR_SQL_REQUEST, ex.getMessage(), log::error);
        }
    }

    protected Film makeFilm(ResultSet rs) throws SQLException {
        Film film = Film.builder()
                .id(rs.getLong("film_id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .duration(rs.getInt("duration"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .build();

        final int ratingId = rs.getInt("rating_id");
        if (ratingId != 0) {
            film.setMpa(ratingMPADbStorage.getById(ratingId));
        }

        final Set<Genre> genres = new TreeSet<>(Comparator.comparing(Genre::getId));
        genres.addAll(genreDbStorage.getByFilmId(film.getId()));
        if (!genres.isEmpty()) film.setGenres(genres);

        final List<Long> likes = filmLikeDbStorage.getUsersLikeId(film.getId());
        if (!likes.isEmpty()) film.setLikes(new HashSet<>(likes));

        return film;
    }
}
