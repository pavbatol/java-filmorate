package ru.yandex.practicum.filmorate.storage.impl.db;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.impl.MpaRating;
import ru.yandex.practicum.filmorate.storage.MpaRatingStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static ru.yandex.practicum.filmorate.validator.impl.ValidatorManager.validateId;

@Slf4j
@Component
@RequiredArgsConstructor
public class MpaRatingDBStorage implements MpaRatingStorage {

    private final static String UPDATE_MPARATING_SQL = "update mpa_ratings m set rating = ?, description = ? where m.rating_id = ?";
    private final static String FIND_ALL_MPARATINGS_SQL = "select * from mpa_ratings";
    private final static String FIND_MPARATING_BY_ID_SQL = "select * from mpa_ratings m where m.rating_id = ?";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public MpaRating add(@NonNull MpaRating mpaRating) {
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
    public MpaRating update(@NonNull MpaRating mpaRating) {
        validateId(this, mpaRating, null);
        jdbcTemplate.update(UPDATE_MPARATING_SQL,
                mpaRating.getName(),
                mpaRating.getDescription(),
                mpaRating.getId());
        return mpaRating;
    }

    @Override
    public List<MpaRating> findAll() {
        return jdbcTemplate.query(FIND_ALL_MPARATINGS_SQL, this::mapRowToMpaRating);
    }

    @Override
    public Optional<MpaRating> findById(Long id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_MPARATING_BY_ID_SQL, this::mapRowToMpaRating, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private MpaRating mapRowToMpaRating(ResultSet rs, int rowNum) throws SQLException {
        return new MpaRating(rs.getLong("rating_id"),
                rs.getString("rating"),
                rs.getString("description"));
    }
}
