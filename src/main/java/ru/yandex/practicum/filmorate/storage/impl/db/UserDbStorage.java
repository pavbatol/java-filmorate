package ru.yandex.practicum.filmorate.storage.impl.db;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.impl.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static ru.yandex.practicum.filmorate.validator.impl.ValidatorManager.getNonNullObject;

@Slf4j
@Component("userDbStorage")
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private final String entityTypeName = "Пользователь";

    @Override
    public User add(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");
        Map<String, Object> values = new HashMap<>();
        values.put("email", user.getEmail());
        values.put("login", user.getLogin());
        values.put("name", user.getName());
        values.put("birthday", user.getBirthday());
        user.setId(simpleJdbcInsert.executeAndReturnKey(values).intValue());
        return findById(user.getId()).orElse(user);
    }

    @Override
    public User update(User user) {
        if (!contains(user.getId())) {
            String message = String.format("Такого id для %s нет: %s", user.getClass().getSimpleName(), user.getId());
            log.error(message);
            throw new NotFoundException(message);
        }
//        String sql = "update users set  email = ?, login = ?, name = ?, birthday = ?"
//                + "where user_id = ?";
        jdbcTemplate.update(UPDATE_USER_SQL,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        updateFriends(user);
        return user;
    }

    @Override
    public User remove(Long id) throws NotFoundException {
        User user = getNonNullObject(this, id);
//        final String deleteUserSql = "delete from users where user_id = ?";
//        final String deleteFriendsSql = "delete from friends where user_id = ? or friend_id = ?";
        if (jdbcTemplate.update(DELETE_USER_FRIENDS_SQL, id) > 0
                && jdbcTemplate.update(DELETE_USER_SQL, id) > 0) {
            return user;
        }
        return null;
    }

    @Override
    public List<User> findAll() {
//        String sql = "select * from users";
        return jdbcTemplate.query(FIND_ALL_USERS_SQL, this::mapRowToUser);
    }

    @Override
    public Optional<User> findById(Long id) {
//        String sql = "select * from users where user_id = ?";
        List<User> query = jdbcTemplate.query(FIND_USER_BY_ID_SQL, this::mapRowToUser, id);
        return query.stream().findFirst();
    }

    @Override
    public List<User> findFriends(Long userId) {
//        String sql = "select * from users where user_id "
//        + "in (select friend_id from friends where user_id = ?)";
        return new ArrayList<>( jdbcTemplate.query(FIND_FRIENDS_BY_USER_ID_SQL, this::mapRowToUser, userId));
    }

    @Override
    public List<User> findMutualFriends(Long userId, Long otherId) {
//        String sql = "select * from users u "
//                + "join ( "
//                + "   select f.fr_id "
//                + "   from ( "
//                + "       select friend_id as fr_id "
//                + "       from friends "
//                + "       where user_id = ?) f "
//                + "   join ( "
//                + "       select friend_id as fr_id "
//                + "       from friends "
//                + "       where user_id = ?) t on t.fr_id = f.fr_id "
//                + ") mutual on u.user_id = mutual.fr_id";
        return new ArrayList<>( jdbcTemplate.query(find_Mutual_Friends_SQL, this::mapRowToUser, userId, otherId));
    }

    private User mapRowToUser(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getLong("user_id"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .friends(getFriendIds(rs.getLong("user_id")))
                .build();
    }

    private Set<Long> getFriendIds(long userId) {
//        String sql = "select friend_id from friends where user_id = ?";
        return new HashSet<>( jdbcTemplate.query(GET_FRIEND_IDS_BY_USER_ID_SQL,
                (rs, rowNum) -> rs.getLong("friend_id"), userId));
    }

    private void updateFriends(@NonNull User user) {
//        String deleteFriendsSql = "delete from friends where user_id = ?";
//        String insertFriendsSql = "insert into friends (user_id, friend_id) values(?, ?)";
        jdbcTemplate.update(DELETE_USER_FRIENDS_SQL, user.getId());
        if (Objects.nonNull(user.getFriends())) {
            user.getFriends().forEach(friendId ->
                    jdbcTemplate.update(INSERT_USER_FRIENDS_SQL, user.getId(),  friendId));
        }
    }
}
