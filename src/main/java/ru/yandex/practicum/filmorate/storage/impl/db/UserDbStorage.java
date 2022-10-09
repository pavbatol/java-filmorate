package ru.yandex.practicum.filmorate.storage.impl.db;

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

@Slf4j
@Component("userDbStorage")
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

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
        String sql = "update users set  email = ?, login = ?, name = ?, birthday = ?"
                + "where user_id = ?";
        jdbcTemplate.update(sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        updateFriends(user);
        return user;
    }

    @Override
    public List<User> findAll() {
        String sql = "select * from users";
        return jdbcTemplate.query(sql, this::mapRowToUser);
    }

    @Override
    public Optional<User> findById(Long id) {
        String sql = "select * from users where user_id = ?";
        List<User> query = jdbcTemplate.query(sql, this::mapRowToUser, id);
        return query.size() > 0 ? Optional.ofNullable(query.get(0)) : Optional.empty();

    }

    private User mapRowToUser(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getLong("user_id"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .friends(getFriends(rs.getLong("user_id")))
                .build();
    }

    private Set<Long> getFriends(long userId) {
        String sql = "select friend_id from friends where user_id = ? and confirmed is true";
        return new HashSet<>( jdbcTemplate.query(sql,
                (rs, rowNum) -> rs.getLong("friend_id"), userId));
    }

    private void updateFriends(User user) {
        String deleteSql = "delete from friends where user_id = ?";
        String insertSql = "insert into friends (user_id, friend_id, confirmed) values(?, ?, ?)";
        jdbcTemplate.update(deleteSql, user.getId());
        if (Objects.nonNull(user.getFriends())) {
            user.getFriends().forEach(friendId ->
                    jdbcTemplate.update(insertSql, user.getId(),  friendId, "true"));
        }
    }
}
