package ru.yandex.practicum.filmorate.storage.impl.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.impl.MpaRating;
import ru.yandex.practicum.filmorate.storage.MpaRatingStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class MpaRatingDBStorage implements MpaRatingStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public MpaRating add(MpaRating mpaRating) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("mpa_ratings")
                .usingGeneratedKeyColumns("rating_id");
        Map<String, Object> values = new HashMap<>();
        values.put("rating", mpaRating.getName());
        values.put("description", mpaRating.getDescription());
        mpaRating.setId(simpleJdbcInsert.executeAndReturnKey(values).intValue());
        return mpaRating;
    }

    @Override
    public MpaRating update(MpaRating mpaRating) {
        if (!contains(mpaRating.getId())) {
            String message = String.format("Такого id для %s нет: %s", mpaRating.getClass().getSimpleName(), mpaRating.getId());
            log.error(message);
            throw new NotFoundException(message);
        }
        String sql = "update mpa_ratings set rating = ?, description = ? where rating_id = ?";
        jdbcTemplate.update(sql,
                mpaRating.getName(),
                mpaRating.getDescription(),
                mpaRating.getId());
        return mpaRating;
    }

    @Override
    public List<MpaRating> findAll() {
        String sql = "select * from mpa_ratings";
        return jdbcTemplate.query(sql, this::mapRowToMpaRating);
    }

    @Override
    public Optional<MpaRating> findById(Long id) {
        String sql = "select * from mpa_ratings where rating_id = ?";
        List<MpaRating> query = jdbcTemplate.query(sql, this::mapRowToMpaRating, id);
        return query.size() > 0 ? Optional.ofNullable(query.get(0)) : Optional.empty();
    }

    MpaRating mapRowToMpaRating(ResultSet rs, int rowNum) throws SQLException {
        return new MpaRating(rs.getLong("rating_id"),
                rs.getString("rating"),
                rs.getString("description")
        );
    }
}
