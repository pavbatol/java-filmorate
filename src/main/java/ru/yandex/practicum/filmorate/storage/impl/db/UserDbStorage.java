package ru.yandex.practicum.filmorate.storage.impl.db;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
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
        throw new RuntimeException("Не удалось удалить пользователя");
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query(FIND_ALL_USERS_SQL, this::mapRowToUser);
    }

    @Override
    public Optional<User> findById(Long id) {
        List<User> query = jdbcTemplate.query(FIND_USER_BY_ID_SQL, this::mapRowToUser, id);
        return query.stream().findFirst();
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
        return jdbcTemplate.update(DELETE_USER_FRIEND_SQL, userId,  friendId) == 1;
    }

    @Override
    public List<User> findFriends(Long userId) {
        return new ArrayList<>( jdbcTemplate.query(FIND_FRIENDS_BY_USER_ID_SQL, this::mapRowToUser, userId));
    }

    @Override
    public List<User> findMutualFriends(Long userId, Long otherId) {
        return new ArrayList<>( jdbcTemplate.query(FIND_MUTUAL_FRIENDS_SQL, this::mapRowToUser, userId, otherId));
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
        return new HashSet<>( jdbcTemplate.query(GET_FRIEND_IDS_BY_USER_ID_SQL,
                (rs, rowNum) -> rs.getLong("friend_id"), userId));
    }

    private void updateFriends(@NonNull User user) {
        jdbcTemplate.update(DELETE_USER_FRIENDS_SQL, user.getId());
        if (Objects.nonNull(user.getFriends())) {
            user.getFriends().forEach(friendId ->
                    jdbcTemplate.update(INSERT_USER_FRIEND_SQL, user.getId(),  friendId));
        }
    }

    public void clear() {
        jdbcTemplate.update(CLEAR_USERS);
    }
}
