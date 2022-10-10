package ru.yandex.practicum.filmorate.storage.impl.db;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.impl.Genre;
import ru.yandex.practicum.filmorate.model.impl.MpaRating;
import ru.yandex.practicum.filmorate.model.impl.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static ru.yandex.practicum.filmorate.validator.impl.ValidatorManager.getNonNullObject;

@Slf4j
@Component("filmDbStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film add(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");
        Map<String, Object> values = new HashMap<>();
        values.put("name", film.getName());
        values.put("description", film.getDescription());
        values.put("release_date", film.getReleaseDate());
        values.put("duration", film.getDuration());
        values.put("rating_id", film.getMpa().getId());
        film.setId(simpleJdbcInsert.executeAndReturnKey(values).longValue());
        updateFilmGenres(film);
        return findById(film.getId()).orElse(film);
    }

    @Override
    public Film update(Film film) {
        if (!contains(film.getId())) {
            String message = String.format("Такого id для %s нет: %s", film.getClass().getSimpleName(), film.getId());
            log.error(message);
            throw new NotFoundException(message);
        }
        String sql = "update films set "
                + "name = ?, description = ?, release_date = ?, duration = ?, rating_id = ? "
                + "where film_id = ?";
        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        updateFilmLikes(film);
        updateFilmGenres(film);
        return film;
    }

    @Override
    public Film remove(Long id) {
        Film film = getNonNullObject(this, id);
        final String sql = "delete from films where film_id = ?";
        return jdbcTemplate.update(sql, id) > 0 ? film : null;
    }

    @Override
    public List<Film> findAll() {
        String sql = "select * from films f "
                + "left join (select rating_id AS ri, rating AS rt, description AS dc "
                + "from mpa_ratings) r on f.rating_id = ri ";
        return jdbcTemplate.query(sql, this::mapRowToFilm);
    }

    @Override
    public Optional<Film> findById(Long id) {
        String sql = "select * from films f "
                + "left join (select rating_id AS ri, rating AS rt, description AS dc "
                + "from mpa_ratings) r on f.rating_id = ri "
                + "where f.film_id = ?";
        List<Film> query = jdbcTemplate.query(sql, this::mapRowToFilm, id);
        return query.size() > 0 ? Optional.ofNullable(query.get(0)) : Optional.empty();
    }

    private Film mapRowToFilm(ResultSet rs, int rowNum) throws SQLException {
        long filmId = rs.getLong("film_id");
        return Film.builder()
                .id(filmId)
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getLong("duration"))
                .likes(getLikesByFilmId(filmId))
                .genres(getGenresByFilmId(filmId))
                .mpa(new MpaRating(rs.getInt("rating_id"),
                        rs.getString("rt"),
                        rs.getString("dc")))
                .build();
    }

    private Set<Genre> getGenresByFilmId(long filmId) {
        String sql = "select * from film_genres fg "
                + "left join genres g on fg.genre_id = g.genre_id "
                + "where fg.film_id = ?";
        return new HashSet<>(jdbcTemplate.query(sql, (rs, rowNum) ->
                new Genre(rs.getInt("genre_id"), rs.getString("name")), filmId));
    }

    private Set<Long> getLikesByFilmId(long filmId) {
        String sql = "select user_id from film_likes where film_id = ?";
        return new HashSet<>(jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong("user_id"), filmId));
    }

    private void updateFilmLikes(@NonNull Film film) {
        String deleteSql = "delete from film_likes where film_id = ?";
        String insertSql = "insert into film_likes (film_id, user_id) values(?, ?)";
        String updateSql = "update films set rate = ? where film_id = ?";
        jdbcTemplate.update(deleteSql, film.getId());
        Optional.ofNullable(film.getLikes()).ifPresent(likes -> {
                    likes.forEach(userId -> jdbcTemplate.update(insertSql, film.getId(), userId));
                    jdbcTemplate.update(updateSql, film.getRate(), film.getId());
                });
    }

    private void updateFilmGenres(@NonNull Film film) {
        String deleteSql = "delete from film_genres where film_id = ?";
        String insertSql = "insert into film_genres (film_id, genre_id) values(?, ?)";
        jdbcTemplate.update(deleteSql, film.getId());
        Optional.ofNullable(film.getGenres()).ifPresent(genres -> genres.stream()
                .map(Genre::getId)
                .forEach(genreId -> jdbcTemplate.update(insertSql, film.getId(), genreId)));
    }
}
