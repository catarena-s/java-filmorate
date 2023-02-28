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
import java.util.List;

@Slf4j
@Component
public class FriendshipDbStorage extends DaoStorage implements FriendStorage {

    @Autowired
    public FriendshipDbStorage(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public List<Friend> getFriends(long userId) {
        final String sql = "SELECT * FROM friendship WHERE user_id = ?";
        try {
            log.debug(LOG_MESSAGE_SQL_REQUEST, sqlQueryToString(sql, userId));
            return jdbcTemplate.query(sql, (rs, rowNum) -> makeFriend(rs), userId);
        } catch (Exception ex) {
            throw new FilmorateException(String.format("Ошибка выполнения sql запроса [%s]",
                    sqlQueryToString(sql, userId)), log::error);
        }
    }

    protected boolean isFriendshipExist(long friendId, long userId) {
        final String sql = "SELECT COUNT(*) FROM friendship WHERE (user_id = ? AND friend_id = ?)";
        final int count = getCount(sql, userId, friendId);
        return (count > 0);
    }

    private Friend makeFriend(ResultSet rs) throws SQLException {
        return Friend.builder()
                .userId(rs.getLong("friend_id"))
                .isConfirmed(rs.getBoolean("friendship_state"))
                .build();
    }
}
