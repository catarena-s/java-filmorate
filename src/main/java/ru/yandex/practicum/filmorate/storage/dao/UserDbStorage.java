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

import static ru.yandex.practicum.filmorate.storage.dao.DaoQueries.*;

@Component
@Primary
@Slf4j
public class UserDbStorage extends DaoStorage implements UserStorage {
    protected static final String USER_ID = "user_id";
    private final FriendshipDbStorage friendshipDAO;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate, FriendshipDbStorage friendshipDAO) {
        super(jdbcTemplate);
        this.friendshipDAO = friendshipDAO;
    }

    @Override
    public User create(User user) {
        throwErrorIfExist(SELECT_COUNT_FROM_USER_INFO_WHERE_LOGIN, "User", "login", user.getLogin(), log::error);
        throwErrorIfExist(SELECT_COUNT_FROM_USER_INFO_WHERE_EMAIL, "User", "email", user.getEmail(), log::error);
        try {
            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("user_info")
                    .usingGeneratedKeyColumns(USER_ID);

            final long userId = simpleJdbcInsert.executeAndReturnKey(user.toMap()).longValue();

            return getById(userId);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new FilmorateException(
                    MSG_INSERT_ERROR,
                    String.format("При добавлении пользователя произошла ошибка: {%s}", user),
                    log::error);
        }
    }

    @Override
    public Collection<User> getAll() {
        Map<Long, Set<Friend>> setFriends = friendshipDAO.getSetFriendsForAllUsers();
        return jdbcTemplate.query(SELECT_FROM_USER_INFO, (rs, rowNum) -> makeUser(rs, setFriends));
    }

    @Override
    public User getById(long id) {
        final String sql = SELECT_FROM_USER_INFO_WHERE_USER_ID;
        try {
            log.debug(LOG_MESSAGE_SQL_REQUEST, sqlQueryToString(sql, id));
            Set<Friend> friendList = new HashSet<>(friendshipDAO.getFriends(id));

            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeUser(rs, friendList), id);
        } catch (EmptyResultDataAccessException ex) {
            log.error(ex.getMessage());
            throw new ItemNotFoundException(
                    String.format(MSG_ITEM_NOT_FOUND, "User", id),
                    log::error);
        }
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
            log.error(ex.getMessage());
            throw new FilmorateException(
                    MSG_UPDATE_ERROR,
                    String.format("При обновлении данных пользователя произошла ошибка: {%s}", user),
                    log::error);
        }
    }

    @Override
    public List<User> getFriendsForUser(long userId) {
        throwExceptionIfNoExistById(SELECT_COUNT_FROM_USER_INFO_WHERE_USER_ID, userId, "User", log);

        final String sql = SELECT_FRIENDS_FROM_USER_INFO_WHERE_USER_ID;
        log.debug(LOG_MESSAGE_SQL_REQUEST, sql, userId);
        Map<Long, Set<Friend>> setFriends = friendshipDAO.getSetFriendsForUsers(userId);
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs, setFriends), userId);
    }

    @Override
    public List<User> getCommonFriends(long userId, long otherId) {
        final String sql = SELECT_COUNT_FROM_USER_INFO_WHERE_USER_ID;
        // проверяем наличие пользователя в бд с id = userId
        throwExceptionIfNoExistById(sql, userId, "User", log);
        // проверяем наличие пользователя в бд с id = otherId
        throwExceptionIfNoExistById(sql, otherId, "User", log);

        final String sqlCommonFriends = "SELECT u.user_id, u.name, u.login,u.email, u.birthday\n" +
                "FROM user_info u INNER JOIN (\n" +
                "    SELECT friend_id  FROM friendship f WHERE user_id = ?\n" +
                "    INTERSECT \n" +
                "    SELECT friend_id  FROM friendship f WHERE user_id = ?\n" +
                ") f ON f.friend_id = u.user_id";

        log.debug(LOG_MESSAGE_SQL_REQUEST, sqlQueryToString(sqlCommonFriends, userId, otherId));

        Map<Long, Set<Friend>> setFriends = friendshipDAO.getSetFriendsForCommonFriends(userId, otherId);

        return jdbcTemplate.query(sqlCommonFriends, (rs, rowNum) -> makeUser(rs, setFriends), userId, otherId);
    }

    @Override
    public User removeFromFriends(long userId, long friendId) {
        final String sql = SELECT_COUNT_FROM_USER_INFO_WHERE_USER_ID;
        // проверяем наличие пользователя в бд с id = userId
        throwExceptionIfNoExistById(sql, userId, "User", log);
        // проверяем наличие пользователя в бд с id = friendId
        throwExceptionIfNoExistById(sql, friendId, "User", log);

        friendshipDAO.removeFromFriendship(userId, friendId);
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
        if (friendshipDAO.isFriendshipExist(userId, friendId)) {
            log.warn("Friend id={} уже добавлен в друзья пользователю id={}", friendId, userId);
            return getById(userId);
        }

        friendshipDAO.addFriendship(userId, friendId);
        return getById(userId);
    }

    private User makeUser(ResultSet rs, Map<Long, Set<Friend>> setFriends) throws SQLException {
        final long userId = rs.getLong(USER_ID);
        Set<Friend> friends = setFriends.getOrDefault(userId, new HashSet<>());

        return makeUser(rs, friends);
    }

    private User makeUser(ResultSet rs, Set<Friend> friendSet) throws SQLException {
        return User.builder()
                .id(rs.getLong(USER_ID))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .friends(friendSet)
                .build();
    }

}
