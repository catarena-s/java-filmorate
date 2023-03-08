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
import ru.yandex.practicum.filmorate.model.RatingMPA;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.storage.dao.DaoQueries.*;

@Component
@Primary
@Slf4j
public class FilmDbStorage extends DaoStorage implements FilmStorage {
    protected static final String FILM_ID = "film_id";
    private final GenreDbStorage genreDbStorage;
    private final FilmGenreDBStorage filmGenresDbStorage;
    private final LikeDbStorage filmLikeDbStorage;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate,
                         GenreDbStorage genreDbStorage,
                         FilmGenreDBStorage filmGenresDbStorage,
                         LikeDbStorage filmLikeDbStorage) {
        super(jdbcTemplate);
        this.genreDbStorage = genreDbStorage;
        this.filmGenresDbStorage = filmGenresDbStorage;
        this.filmLikeDbStorage = filmLikeDbStorage;
    }

    @Override
    public Collection<Film> getAll() {
        log.debug(LOG_MESSAGE_SQL_REQUEST, SELECT_FROM_FILM);
        Collection<Genre> genres = genreDbStorage.getAll();
        Map<Long, Set<Long>> filmsGenres = filmGenresDbStorage.getSetGenresFroAllFilms();
        Map<Long, Set<Long>> filmLikes = filmLikeDbStorage.getSetLikesForAllFilms();

        return jdbcTemplate.query(SELECT_FROM_FILM, (rs, rowNum) -> makeFilm(rs, genres, filmsGenres, filmLikes));
    }

    @Override
    public Film getById(long id) {
        try {
            log.debug(LOG_MESSAGE_SQL_REQUEST, sqlQueryToString(SELECT_FROM_FILM_WHERE_FILM_ID, id));

            Set<Genre> setGenres = new HashSet<>(genreDbStorage.getByFilmId(id));
            Set<Long> likes = new HashSet<>(filmLikeDbStorage.getUsersLikeId(id));

            return jdbcTemplate.queryForObject(SELECT_FROM_FILM_WHERE_FILM_ID,
                    (rs, rowNum) -> makeFilm(rs, setGenres, likes), id);
        } catch (EmptyResultDataAccessException ex) {
            throw new ItemNotFoundException(
                    String.format(MSG_ITEM_NOT_FOUND, "Film", id),
                    log::error);
        }
    }

    @Override
    public Film create(Film film) {
        try {
            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("film")
                    .usingGeneratedKeyColumns(FILM_ID);

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
            log.error(ex.getMessage());
            throw new FilmorateException(
                    MSG_INSERT_ERROR,
                    String.format("При добавлении фильма произошла ошибка: {%s}", film),
                    log::error);
        }
    }

    @Override
    public List<Film> getTopByLikes(int top) {
        Collection<Genre> genres = genreDbStorage.getAll();
        Map<Long, Set<Long>> filmGenres = filmGenresDbStorage.getSetGenresForTopFilms(top);
        Map<Long, Set<Long>> filmLikes = filmLikeDbStorage.getSetLikesForTopFilms(top);

        final String sql =
                "SELECT f.*, r.name as r_name  \n" +
                        "FROM FILM f LEFT JOIN likes l ON f.FILM_ID = l.FILM_ID \n" +
                        "LEFT JOIN rating r ON r.rating_id =f.rating_id \n" +
                        "GROUP BY f.film_id, r.name ORDER BY count(l.film_id) DESC, f.film_id ASC \n" +
                        "LIMIT ?";

        try {
            log.debug(LOG_MESSAGE_SQL_REQUEST, sqlQueryToString(sql, top));

            return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs, genres, filmGenres, filmLikes), top);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new FilmorateException(
                    "Ошибка получения списка популярных фильмов",
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
                log.error(ex.getMessage());
                throw new FilmorateException(
                        MSG_INSERT_ERROR,
                        String.format("При добавлении лайка(userID= %d) фильму(filmID= %d) произошла ошибка", userId, filmId),
                        log::error);
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
            log.error(ex.getMessage());
            throw new FilmorateException(
                    MSG_UPDATE_ERROR,
                    String.format("При обновлении данных фильма произошла ошибка: {%s}", film),
                    log::error);
        }
    }


    private Film makeFilm(ResultSet rs,
                          Collection<Genre> genres,
                          Map<Long, Set<Long>> filmsGenres,
                          Map<Long, Set<Long>> filmLikes) throws SQLException {

        final long filmId = rs.getLong(FILM_ID);
        Set<Long> filmGenreIds = Optional.ofNullable(filmsGenres.get(filmId)).orElse(new HashSet<>());
        Set<Long> likes = Optional.ofNullable(filmLikes.get(filmId)).orElse(new HashSet<>());

        final Set<Genre> setGenres = genres.stream()
                .filter(f -> filmGenreIds.contains(f.getId()))
                .collect(Collectors.toSet());


        return makeFilm(rs, setGenres, likes);
    }


    private Film makeFilm(ResultSet rs,
                          Set<Genre> setGenres,
                          Set<Long> likes) throws SQLException {
        final long filmId = rs.getLong(FILM_ID);

        RatingMPA mpa = RatingMPA.builder()
                .id(rs.getInt("rating_id"))
                .name(rs.getString("r_name"))
                .build();

        return Film.builder()
                .id(filmId)
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .duration(rs.getInt("duration"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .mpa(mpa)
                .genres(setGenres)
                .likes(new HashSet<>(likes))
                .build();
    }
}
