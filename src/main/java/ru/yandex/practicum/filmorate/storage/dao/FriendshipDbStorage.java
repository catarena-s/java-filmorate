package ru.yandex.practicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmorateException;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.storage.FriendStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static ru.yandex.practicum.filmorate.storage.dao.DaoQueries.DELETE_FROM_FRIENDSHIP_WHERE_USER_ID_AND_FRIEND_ID;
import static ru.yandex.practicum.filmorate.storage.dao.DaoQueries.INSERT_INTO_FRIENDS_USER_ID_FRIEND_ID_VALUES;

@Slf4j
@Component
public class FriendshipDbStorage extends DaoStorage implements FriendStorage {

    @Autowired
    public FriendshipDbStorage(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public boolean isFriendshipExist(long userId, long friendId) {
        final String sql = "SELECT COUNT(*) FROM friendship WHERE (user_id = ? AND friend_id = ?)";
        final int count = getCount(sql, userId, friendId);
        return (count > 0);
    }

    @Override
    public void removeFromFriendship(long userId, long friendId) {
        final String sql = DELETE_FROM_FRIENDSHIP_WHERE_USER_ID_AND_FRIEND_ID;
        log.debug(LOG_MESSAGE_SQL_REQUEST, sqlQueryToString(sql, userId, friendId));

        final int count = jdbcTemplate.update(sql, userId, friendId);
        log.debug(MGS_WAS_REMOVE_LINES_FROM_TABLE + " friends", count);
    }

    @Override
    public void addFriendship(long userId, long friendId) {
        String sql = INSERT_INTO_FRIENDS_USER_ID_FRIEND_ID_VALUES;
        log.debug(LOG_MESSAGE_SQL_REQUEST, sqlQueryToString(sql, userId, friendId));
        try {
            jdbcTemplate.update(sql, userId, friendId);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new FilmorateException(
                    MSG_INSERT_ERROR,
                    String.format("При добавлении друга(friendId = %d) пользователю(userId= %d) произошла ошибка", friendId, userId),
                    log::error);
        }
    }

    @Override
    public List<Friend> getFriends(long userId) {
        final String sql = "SELECT * FROM friendship WHERE user_id = ?";
        try {
            log.debug(LOG_MESSAGE_SQL_REQUEST, sqlQueryToString(sql, userId));
            return jdbcTemplate.query(sql, (rs, rowNum) -> makeFriend(rs), userId);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new FilmorateException(
                    "Ошибка получения друзей",
                    String.format("При получении друзей пользователя(userId=%d) произошла ошибка", userId),
                    log::error);
        }
    }

    @Override
    public Map<Long, Set<Friend>> getSetFriendsForUsers(long userId) {
        final String sql = "SELECT ui.* \n" +
                "FROM friendship AS ui \n" +
                "RIGHT JOIN friendship AS f ON ui.user_id = f.friend_id  \n" +
                "WHERE f.user_id = ?";
        log.debug(LOG_MESSAGE_SQL_REQUEST, sqlQueryToString(sql, userId));
        Map<Long, Set<Friend>> setFriends = new HashMap<>();

        jdbcTemplate.query(sql, (rs, rowNum) -> mapper(rs, setFriends), userId);
        return setFriends;
    }

    @Override
    public Map<Long, Set<Friend>> getSetFriendsForCommonFriends(long userId, long otherId) {
        final String sql = "SELECT u.* FROM friendship AS u RIGHT JOIN (\n" +
                "    SELECT friend_id  FROM friendship AS f WHERE user_id = ?\n" +
                "    INTERSECT \n" +
                "    SELECT friend_id  FROM friendship AS f WHERE user_id = ? ) f ON f.friend_id = u.user_id";
        log.debug(LOG_MESSAGE_SQL_REQUEST, sqlQueryToString(sql, userId, otherId));
        Map<Long, Set<Friend>> setFriends = new HashMap<>();
        jdbcTemplate.query(sql, (rs, rowNum) -> mapper(rs, setFriends), userId, otherId);
        return setFriends;
    }

    @Override
    public Map<Long, Set<Friend>> getSetFriendsForAllUsers() {
        final String sql = "SELECT * FROM friendship";
        log.debug(LOG_MESSAGE_SQL_REQUEST, sqlQueryToString(sql));
        Map<Long, Set<Friend>> setFriends = new HashMap<>();
        jdbcTemplate.query(sql, (rs, rowNum) -> mapper(rs, setFriends));
        return setFriends;
    }

    private Object mapper(ResultSet rs, Map<Long, Set<Friend>> setFriends) throws SQLException {
        final long film_id = rs.getLong("user_id");
        Set<Friend> set = setFriends.getOrDefault(film_id, new HashSet<>());
        set.add(makeFriend(rs));
        setFriends.put(film_id, set);
        return setFriends;

    }

    private Friend makeFriend(ResultSet rs) throws SQLException {
        return Friend.builder()
                .userId(rs.getLong("friend_id"))
                .isConfirmed(rs.getBoolean("friendship_state"))
                .build();
    }
}
