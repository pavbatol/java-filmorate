package ru.yandex.practicum.filmorate.storage.impl.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.impl.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class GenreDBStorage  implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Genre add(Genre genre) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("genres")
                .usingGeneratedKeyColumns("genre_id");
        Map<String, Object> values = new HashMap<>();
        values.put("name", genre.getName());
        genre.setId(simpleJdbcInsert.executeAndReturnKey(values).intValue());
        return genre;
    }

    @Override
    public Genre update(Genre genre) {
        if (!contains(genre.getId())) {
            String message = String.format("Такого id для %s нет: %s", genre.getClass().getSimpleName(), genre.getId());
            log.error(message);
            throw new NotFoundException(message);
        }
        String sql = "update genres set name = ? where genre_id = ?";
        jdbcTemplate.update(sql,
                genre.getName(),
                genre.getId());
        return genre;
    }

    @Override
    public List<Genre> findAll() {
        String sql = "select * from genres";
        return jdbcTemplate.query(sql, this::mapRowToGenre);
    }

    @Override
    public Optional<Genre> findById(Long id) {
        String sql = "select * from genres where genre_id = ?";
        List<Genre> query = jdbcTemplate.query(sql, this::mapRowToGenre, id);
        return query.stream().findFirst();
    }

    private Genre mapRowToGenre(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(rs.getLong("genre_id"), rs.getString("name"));
    }
}
