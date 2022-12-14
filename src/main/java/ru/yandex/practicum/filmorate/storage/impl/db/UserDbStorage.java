package ru.yandex.practicum.filmorate.storage.impl.db;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.impl.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static ru.yandex.practicum.filmorate.validator.impl.ValidatorManager.getNonNullObject;
import static ru.yandex.practicum.filmorate.validator.impl.ValidatorManager.validateId;

@Slf4j
@Component("userDbStorage")
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final static String UPDATE_USER_SQL = "update users set email = ?, login = ?, name = ?, birthday = ?"
            + "where user_id = ?";

    private final static String DELETE_USER_SQL = "delete from users u where u.user_id = ?";

    private final static String FIND_ALL_USERS_SQL = "select * from users";

    private final static String FIND_USER_BY_ID_SQL = "select * from users u where u.user_id = ?";

    private final static String FIND_FRIENDS_BY_USER_ID_SQL = "select u.* from friends f "
            + "join users u on f.friend_id = u.user_id "
            + "where f.user_id = ?";

    private final static String FIND_FRIENDS_WITH_MARK_BY_USER_ID_SQL = "select u.*, "
            + "case when f2.friend_id is not null then true else false end confirmed "
            + "from friends f "
            + "left join friends f2 on f.friend_id = f2.user_id and f.user_id = ? and f2.friend_id = ? "
            + "join users u on f.friend_id = u.user_id "
            + "where f.user_id = ?";

    private final static String FIND_CONFIRMED_FRIENDS_BY_USER_ID_SQL = "select u.* from friends f "
            + "join friends f2 on f.friend_id = f2.user_id "
            + "join users u on f.friend_id = u.user_id "
            + "where f.user_id = ? and f2.friend_id = ?";

    private final static String FIND_NOT_CONFIRMED_FRIENDS_BY_USER_ID_SQL = "select u.* from friends f "
            + "left join friends f2 on f.friend_id = f2.user_id and f.user_id = ? and f2.friend_id = ? "
            + "join users u on f.friend_id = u.user_id "
            + "where f.user_id = ? and f2.friend_id is null";

    private final static String FIND_MUTUAL_FRIENDS_SQL = "select u.* from friends f "
            + "join friends f2 on f.friend_id = f2.friend_id "
            + "join users u on f.friend_id = u.user_id "
            + "where f.user_id = ? and f2.user_id = ? ";

    private final static String INSERT_USER_FRIEND_SQL = "insert into friends (user_id, friend_id) values(?, ?)";

    private final static String DELETE_USER_FRIEND_SQL = "delete from friends f where f.user_id = ? and f.friend_id = ?";

    private final static String DELETE_ALL_USER_FRIENDS_SQL = "delete from friends f where f.user_id = ?";

    private final static String GET_FRIEND_IDS_BY_USER_ID_SQL = "select f.friend_id from friends f where f.user_id = ?";

    private final static String CLEAR_USERS = "delete from users";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public User add(@NonNull User user) {
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
    public User update(@NonNull User user) {
        validateId(this, user, null);
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
    public User remove(Long id) {
        User user = getNonNullObject(this, id);
        if (jdbcTemplate.update(DELETE_USER_SQL, id) > 0) {
            return user;
        }
        throw new RuntimeException("???? ?????????????? ?????????????? ????????????????????????");
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query(FIND_ALL_USERS_SQL, this::mapRowToUser);
    }

    @Override
    public Optional<User> findById(Long id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_USER_BY_ID_SQL, this::mapRowToUser, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean contains(Long id) {
        final  String sql = "select 1 from users u where u.user_id = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, id);
        return sqlRowSet.first();
    }

    @Override
    public boolean addFriend(Long userId, Long friendId) {
        try {
            return jdbcTemplate.update(INSERT_USER_FRIEND_SQL, userId, friendId) == 1;
        } catch (DataAccessException e) {
            return false;
        }
    }

    @Override
    public boolean removeFriend(Long userId, Long friendId) {
        return jdbcTemplate.update(DELETE_USER_FRIEND_SQL, userId, friendId) == 1;
    }

    @Override
    public List<User> findFriends(Long userId) {
        return jdbcTemplate.query(FIND_FRIENDS_BY_USER_ID_SQL, this::mapRowToUser, userId);
    }

    @Override
    public List<User> findMutualFriends(Long userId, Long otherId) {
        return jdbcTemplate.query(FIND_MUTUAL_FRIENDS_SQL, this::mapRowToUser, userId, otherId);
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
        return new HashSet<>(jdbcTemplate.query(GET_FRIEND_IDS_BY_USER_ID_SQL,
                (rs, rowNum) -> rs.getLong("friend_id"), userId));
    }

    private void updateFriends(@NonNull User user) {
        jdbcTemplate.update(DELETE_ALL_USER_FRIENDS_SQL, user.getId());
        Optional.ofNullable(user.getFriends()).ifPresent(ids ->
                jdbcTemplate.batchUpdate(INSERT_USER_FRIEND_SQL, ids, ids.size(), (ps, id) -> {
                    ps.setLong(1, user.getId());
                    ps.setLong(2, id);
                }));
    }

    public void clear() {
        jdbcTemplate.update(CLEAR_USERS);
    }
}
