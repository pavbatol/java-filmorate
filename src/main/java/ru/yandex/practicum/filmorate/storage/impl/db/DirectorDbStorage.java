package ru.yandex.practicum.filmorate.storage.impl.db;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.impl.Director;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static ru.yandex.practicum.filmorate.validator.impl.ValidatorManager.getNonNullObject;
import static ru.yandex.practicum.filmorate.validator.impl.ValidatorManager.validateId;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DirectorDbStorage implements DirectorStorage {

    private final static String UPDATE_SQL = "update directors d set name = ? where d.director_id = ?";
    private final static String REMOVE_SQL = "delete from directors d where d.director_id = ?";
    private final static String FIND_ALL_SQL = "select * from directors";
    private final static String FIND_BY_ID_SQL = "select * from directors d where d.director_id = ?";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Director add(@NonNull Director director) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("directors")
                .usingGeneratedKeyColumns("director_id");
        Map<String, Object> values = new HashMap<>();
        values.put("name", director.getName());
        director.setId(simpleJdbcInsert.executeAndReturnKey(values).intValue());
        return director;
    }

    @Override
    public Director update(@NonNull Director director) {
        validateId(this, director, null);
        jdbcTemplate.update(UPDATE_SQL, director.getName(), director.getId());
        return director;
    }

    @Override
    public Director remove(Long id) {
        Director director = getNonNullObject(this, id);
        jdbcTemplate.update(REMOVE_SQL, id);
        return director;
    }

    @Override
    public List<Director> findAll() {
        return jdbcTemplate.query(FIND_ALL_SQL, this::mapRowToGenre);
    }

    @Override
    public Optional<Director> findById(Long id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_BY_ID_SQL, this::mapRowToGenre, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean contains(Long id) {
        final  String sql = "select 1 from directors d where d.director_id = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, id);
        return sqlRowSet.first();
    }

    private Director mapRowToGenre(ResultSet rs, int rowNum) throws SQLException {
        return new Director(rs.getLong("director_id"), rs.getString("name"));
    }
}
