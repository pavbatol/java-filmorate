package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.impl.User;

import java.util.List;

public interface UserStorage extends Storage<User> {

    final static String UPDATE_USER_SQL = "update users set  email = ?, login = ?, name = ?, birthday = ?"
            + "where user_id = ?";

    final static String DELETE_USER_SQL = "delete from users where user_id = ?";

    final static String FIND_ALL_USERS_SQL = "select * from users";

    final static String FIND_USER_BY_ID_SQL = "select * from users where user_id = ?";

    final static String FIND_FRIENDS_BY_USER_ID_SQL = "select * from users where user_id "
            + "in (select friend_id from friends where user_id = ?)";

    final static String FIND_MUTUAL_FRIENDS_SQL = "select * from users u "
            + "join ( "
            + "   select f.fr_id "
            + "   from ( "
            + "       select friend_id as fr_id "
            + "       from friends "
            + "       where user_id = ?) f "
            + "   join ( "
            + "       select friend_id as fr_id "
            + "       from friends "
            + "       where user_id = ?) t on t.fr_id = f.fr_id "
            + ") mutual on u.user_id = mutual.fr_id";

    final static String INSERT_USER_FRIEND_SQL = "insert into friends (user_id, friend_id) values(?, ?)";

    final static String DELETE_USER_FRIEND_SQL = "delete from friends where user_id = ? and friend_id = ?";

    final static String DELETE_USER_FRIENDS_SQL = "delete from friends where user_id = ?";

    final static String GET_FRIEND_IDS_BY_USER_ID_SQL = "select friend_id from friends where user_id = ?";

    final static String CLEAR_USERS = "delete from users";

    boolean addFriend(Long userId, Long friendId);

    boolean removeFriend(Long userId, Long friendId);

    List<User> findFriends(Long userId);

    List<User> findMutualFriends(Long userId, Long otherId);
}
