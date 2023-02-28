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
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.storage.dao.DaoQueries.*;

@Component
@Primary
@Slf4j
public class UserDbStorage extends DaoStorage implements UserStorage {
    private final FriendshipDbStorage friendDAO;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate, FriendshipDbStorage friendDAO) {
        super(jdbcTemplate);
        this.friendDAO = friendDAO;
    }

    @Override
    public User create(User user) {
        throwErrorIfExist(SELECT_COUNT_FROM_USER_INFO_WHERE_LOGIN, "User", "login", user.getLogin(), log::error);
        throwErrorIfExist(SELECT_COUNT_FROM_USER_INFO_WHERE_EMAIL, "User", "email", user.getEmail(), log::error);
        try {
            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("user_info")
                    .usingGeneratedKeyColumns("user_id");

            final long userId = simpleJdbcInsert.executeAndReturnKey(user.toMap()).longValue();

            return getById(userId);
        } catch (Exception ex) {
            throw new FilmorateException("Ошибка добавления в таблицу user_info", ex.getMessage(), log::error);
        }
    }

    @Override
    public Collection<User> getAll() {
        return jdbcTemplate.query(SELECT_FROM_USER_INFO, (rs, rowNum) -> makeUser(rs));
    }

    @Override
    public User update(User user) {
        throwExceptionIfNoExistById(SELECT_COUNT_FROM_USER_INFO_WHERE_USER_ID, user.getId(), "User", log);

        final String sql = UPDATE_USER_INFO;
        log.debug(LOG_MESSAGE_SQL_REQUEST,
                sqlQueryToString(sql, user.getName(), user.getLogin(), user.getEmail(), user.getBirthday(), user.getId()));
        try {
            jdbcTemplate.update(sql, user.getName(), user.getLogin(), user.getEmail(), user.getBirthday(), user.getId());
            return getById(user.getId());
        } catch (Exception ex) {
            throw new FilmorateException(
                    String.format(MSG_ERROR_SQL_QUERY, sql),
                    ex.getMessage(),
                    log::error);
        }
    }


    @Override
    public User getById(long id) {
        final String sql = SELECT_FROM_USER_INFO_WHERE_USER_ID;
        try {
            log.debug(LOG_MESSAGE_SQL_REQUEST, sqlQueryToString(sql, id));
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeUser(rs), id);
        } catch (EmptyResultDataAccessException ex) {
            throw new ItemNotFoundException(
                    String.format(MSG_ITEM_NOT_FOUND, "User", id),
                    ex.getMessage(),
                    log::error);
        }
    }

    public List<User> getByIds(List<Friend> friends) {
        final String sql = String.format(SELECT_FROM_USER_INFO_WHERE_USER_ID_IN_S,
                friends.stream()
                        .map(Friend::getUserId)
                        .map(String::valueOf)
                        .collect(Collectors.joining(",")));
        try {
            log.debug(LOG_MESSAGE_SQL_REQUEST, sql);
            return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
        } catch (Exception ex) {
            throw new FilmorateException(
                    String.format(MSG_ERROR_SQL_QUERY, sql),
                    ex.getMessage(),
                    log::error);
        }
    }

    @Override
    public List<User> getFriendsForUser(long userId) {
        throwExceptionIfNoExistById(SELECT_COUNT_FROM_USER_INFO_WHERE_USER_ID, userId, "User", log);

        final List<Friend> friends = friendDAO.getFriends(userId);
        if (friends.isEmpty()) return Collections.emptyList();

        return getByIds(friends);
    }

    @Override
    public List<User> getCommonFriends(long userId, long otherId) {
        final String sql = SELECT_COUNT_FROM_USER_INFO_WHERE_USER_ID;
        // проверяем наличие пользователя в бд с id = userId
        throwExceptionIfNoExistById(sql, userId, "User", log);
        // проверяем наличие пользователя в бд с id = otherId
        throwExceptionIfNoExistById(sql, otherId, "User", log);

        final Set<Friend> user1Friends = new HashSet<>(friendDAO.getFriends(userId));
        final Set<Friend> user2Friends = new HashSet<>(friendDAO.getFriends(otherId));
        user1Friends.retainAll(user2Friends);

        if (user1Friends.isEmpty()) return Collections.emptyList();

        return getByIds(new ArrayList<>(user1Friends));
    }

    @Override
    public User removeFromFriends(long userId, long friendId) {
        String sql = SELECT_COUNT_FROM_USER_INFO_WHERE_USER_ID;
        // проверяем наличие пользователя в бд с id = userId
        throwExceptionIfNoExistById(sql, userId, "User", log);
        // проверяем наличие пользователя в бд с id = friendId
        throwExceptionIfNoExistById(sql, friendId, "User", log);

        sql = DELETE_FROM_FRIENDSHIP_WHERE_USER_ID_AND_FRIEND_ID;
        log.debug(LOG_MESSAGE_SQL_REQUEST, sqlQueryToString(sql, userId, friendId));
        final int count = jdbcTemplate.update(sql, userId, friendId);
        log.debug("Было удалено {} строк из таблицы friends", count);
        return getById(userId);
    }

    @Override
    public User addFriend(long userId, long friendId) {
        String sql = SELECT_COUNT_FROM_USER_INFO_WHERE_USER_ID;
        // проверяем наличие пользователя в бд с id = userId
        throwExceptionIfNoExistById(sql, userId, "User", log);
        // проверяем наличие пользователя в бд с id = friendId
        throwExceptionIfNoExistById(sql, friendId, "User", log);
        // проверяем наличие записи в таблице friendship
        if (friendDAO.isFriendshipExist(friendId, userId)) {
            log.warn("Friend id={} уже добавлен в друзья пользователю id={}", friendId, userId);
            return getById(userId);
        }

        sql = INSERT_INTO_FRIENDS_USER_ID_FRIEND_ID_VALUES;
        log.debug(LOG_MESSAGE_SQL_REQUEST, sqlQueryToString(sql, userId, friendId));
        try {
            jdbcTemplate.update(sql, userId, friendId);
            return getById(userId);
        } catch (Exception ex) {
            throw new FilmorateException(String.format("Ошибка выполнения sql запроса %s", sql),
                    ex.getMessage(), log::error);
        }
    }

    private User makeUser(ResultSet rs) throws SQLException {
        User user = User.builder()
                .id(rs.getLong("user_id"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .build();
        final List<Friend> friends = friendDAO.getFriends(user.getId());
        user.setFriends(new HashSet<>(friends));
        return user;
    }

}
