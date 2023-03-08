package ru.yandex.practicum.filmorate.storage.dao;

public class DaoQueries {
    private DaoQueries() {
    }
    //------------------------------------------------------------------------------------------------------------------
    //-- FILM -----
    public static final String SELECT_FROM_FILM =
            "SELECT f.*, r.name AS r_name " +
                    "FROM film f LEFT join rating r ON f.rating_id = r.rating_id ";
    protected static final String WHERE_FILM_ID = " WHERE film_id = ?";
    public static final String SELECT_COUNT_FROM_FILM_WHERE_FILM_ID = "SELECT COUNT(*) FROM film" + WHERE_FILM_ID;
    public static final String SELECT_FROM_FILM_WHERE_FILM_ID = SELECT_FROM_FILM + WHERE_FILM_ID;
    public static final String UPDATE_FILM =
            "UPDATE film SET name =?, description =?, duration =?, release_date =?, rating_id =? " + WHERE_FILM_ID;
    public static final String DELETE_FROM_FILM_GENRE_WHERE_FILM_ID = "DELETE FROM film_genre " + WHERE_FILM_ID;
    //------------------------------------------------------------------------------------------------------------------
    //-- USER ----
    public static final String SELECT_FROM_USER_INFO = "SELECT * FROM user_info";
    public static final String SELECT_COUNT_FROM_USER_INFO = "SELECT COUNT(*) FROM user_info ";
    public static final String SELECT_COUNT_FROM_USER_INFO_WHERE_EMAIL = SELECT_COUNT_FROM_USER_INFO + "WHERE email=?";
    public static final String SELECT_COUNT_FROM_USER_INFO_WHERE_LOGIN = SELECT_COUNT_FROM_USER_INFO + "WHERE login=?";
    public static final String UPDATE_USER_INFO = "UPDATE user_info SET name=?, login=?, email=?, birthday=? WHERE user_id = ?";
    public static final String SELECT_FROM_USER_INFO_WHERE_USER_ID = SELECT_FROM_USER_INFO + " WHERE user_id = ?";
    public static final String SELECT_COUNT_FROM_USER_INFO_WHERE_USER_ID = SELECT_COUNT_FROM_USER_INFO + " WHERE user_id = ?";
    //------------------------------------------------------------------------------------------------------------------
    //-- friendship
    public static final String SELECT_FRIENDS_FROM_USER_INFO_WHERE_USER_ID =
            "SELECT ui.* FROM user_info AS ui \n" +
                    "LEFT JOIN friendship AS f ON ui.user_id = f.friend_id \n" +
                    "WHERE f.user_id =?";
    public static final String DELETE_FROM_FRIENDSHIP_WHERE_USER_ID_AND_FRIEND_ID
            = "DELETE FROM friendship WHERE (user_id = ? AND friend_id = ?)";
    public static final String INSERT_INTO_FRIENDS_USER_ID_FRIEND_ID_VALUES
            = "INSERT INTO friendship(user_id,friend_id) VALUES (?, ?) ";
    public static final String INSERT_INTO_LIKES = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";
    public static final String DELETE_FROM_LIKES = "DELETE FROM likes " + WHERE_FILM_ID + " AND user_id = ?";
    public static final String INSERT_INTO_FILM_GENRE = "INSERT INTO film_genre(film_id, genre_id) VALUES(? , ? ) ";
    //------------------------------------------------------------------------------------------------------------------

}
