package ru.yandex.practicum.filmorate.storage.impl.db;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.enums.SortByType;
import ru.yandex.practicum.filmorate.model.impl.Film;
import ru.yandex.practicum.filmorate.model.impl.Genre;
import ru.yandex.practicum.filmorate.model.impl.MpaRating;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.validator.impl.ValidatorManager.getNonNullObject;

@Slf4j
@Validated
@Component("filmDbStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final static String UPDATE_FILM_SQL = "update films f set "
            + "name = ?, description = ?, release_date = ?, duration = ?, rating_id = ? , rate = ?"
            + "where f.film_id = ?";

    private final static String DELETE_FILM_SQL = "delete from films f where f.film_id = ?";

    private final static String FIND_ALL_FILM_SQL = "select f.*, r.rating_id as ri, r.rating as rt, r.description as dc "
            + "from films f "
            + "left join mpa_ratings r on f.rating_id = r.rating_id";

    private final static String FIND_FILM_BY_ID_SQL = "select f.*, r.rating_id as ri, r.rating as rt, r.description as dc "
            + "from films f "
            + "left join mpa_ratings r on f.rating_id = r.rating_id "
            + "where film_id = ?";

    private final static String FIND_POPULAR_FILMS_SQL = FIND_ALL_FILM_SQL + " "
            + "order by rate desc limit ?";

    private final static String DELETE_LIKES_BY_FILM_ID_SQL = "delete from film_likes f where f.film_id = ?";

    private final static String DELETE_LIKE_BY_FILM_ID_AND_USER_ID_SQL = "delete from film_likes f where f.film_id = ? and f.user_id - ?";

    private final static String INSERT_LIKE_BY_FILM_ID_AND_USER_ID_SQL = "insert into film_likes (film_id, user_id) values(?, ?)";

    private final static String GET_LIKES_BY_FILM_ID_SQL = "select user_id from film_likes f where f.film_id = ?";

    private final static String INSERT_GENRE_BY_FILM_ID_ANG_GENRE_ID_SQL = "insert into film_genres (film_id, genre_id) values(?, ?)";

    private final static String DELETE_GENRES_BY_FILM_ID_SQL = "delete from film_genres f where f.film_id = ?";

    private final static String GET_GENRES_BY_FILM_ID_SQL = "select * from film_genres fg "
            + "left join genres g on fg.genre_id = g.genre_id "
            + "where fg.film_id = ?";

    private final static String UPDATE_FILM_RATE_WITH_VALUE_SQL = "update films f set rate = ? where f.film_id = ?";

    private final static String UPDATE_FILM_RATE_WITH_INCREMENT_SQL = "update films f set rate = rate + 1 where f.film_id = ?";
    private final static String UPDATE_FILM_RATE_WITH_DECREMENT_SQL = "update films f set rate = rate - 1 where f.film_id = ?";

    private final static String CLEAR_FILMS = "delete from films";

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
        values.put("rate", 0);
        film.setId(simpleJdbcInsert.executeAndReturnKey(values).longValue());
        updateFilmGenres(film);
        return getNonNullObject(this, film.getId());
    }

    @Override
    public Film update(Film film) {
        if (!contains(film.getId())) {
            String message = String.format("Такого id для %s нет: %s", film.getClass().getSimpleName(), film.getId());
            log.error(message);
            throw new NotFoundException(message);
        }
        jdbcTemplate.update(UPDATE_FILM_SQL,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                0,
                film.getId());
        updateFilmLikes(film);
        updateFilmGenres(film);
        return getNonNullObject(this, film.getId());
    }

    @Override
    public Film remove(Long id) {
        Film film = getNonNullObject(this, id);
        if (jdbcTemplate.update(DELETE_FILM_SQL, id) > 0) {
            return film;
        }
        throw new RuntimeException("Не удалось удалить фильм");
    }

    @Override
    public List<Film> findAll() {
        return jdbcTemplate.query(FIND_ALL_FILM_SQL, this::mapRowToFilm);
    }

    @Override
    public Optional<Film> findById(Long id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_FILM_BY_ID_SQL, this::mapRowToFilm, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean addLike(Long filmId, Long userId) {
        try {
            return jdbcTemplate.update(INSERT_LIKE_BY_FILM_ID_AND_USER_ID_SQL, filmId, userId) == 1
                    && jdbcTemplate.update(UPDATE_FILM_RATE_WITH_INCREMENT_SQL, filmId) == 1;
        } catch (DataAccessException e) {
            return false;
        }
    }

    @Override
    public boolean removeLike(Long filmId, Long userId) {
        try {
            return jdbcTemplate.update(DELETE_LIKE_BY_FILM_ID_AND_USER_ID_SQL, filmId, userId) == 1
                    && jdbcTemplate.update(UPDATE_FILM_RATE_WITH_DECREMENT_SQL, filmId) == 1;
        } catch (DataAccessException e) {
            return false;
        }
    }

    @Override
    public List<Film> findPopularFilms(int count) {
        return jdbcTemplate.query(FIND_POPULAR_FILMS_SQL, this::mapRowToFilm, count);
    }

    @Override
    public List<Film> findByDirectorId(Long directorId, @NonNull List<SortByType> sortTypes) {
        class pass {
            public String sortTypeToFieldName(SortByType sortByType) {
                switch (sortByType) {
                    case YEAR: return "f.release_date";
                    case LIKES: return "fl";
                    default: throw new IllegalArgumentException(String.format("Не соответствие для %s: %s",
                                SortByType.class.getSimpleName(), sortTypes));
                }
            }
        }

        List<String> fields = new LinkedHashSet<>(sortTypes).stream()
                .map(sortByType -> new pass().sortTypeToFieldName(sortByType))
                .collect(Collectors.toList());

        String questionMarks = "?,".repeat(fields.size());
        questionMarks = questionMarks.substring(0, questionMarks.length() - 1);

//        System.out.println(questionMarks);

        String FIND_FILMS_BY_DIRECTOR_WITH_SORT = "select * from films f " +
                "join film_directors fd on f.film_id = fd.film_id " +
                "left join film_likes fl on f.film_id = fl.film_id " +
                "where fd.director_id = ? " +
                "order by " + questionMarks;

//        System.out.println(FIND_FILMS_BY_DIRECTOR_WITH_SORT);

        return jdbcTemplate.query(FIND_FILMS_BY_DIRECTOR_WITH_SORT, this::mapRowToFilm, directorId, fields);

//        return null;
    }

//    private String sortTypeToFieldName(SortByType sortByType) {
//        switch (sortByType) {
//            case YEAR: return "release_date";
//            case LIKES: return "fl";
//            default: throw  new IllegalArgumentException();
//        }
//    }

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
                .rate(rs.getInt("rate"))
                .build();
    }

    private Set<Long> getLikesByFilmId(long filmId) {
        return new HashSet<>(jdbcTemplate.query(GET_LIKES_BY_FILM_ID_SQL,
                (rs, rowNum) -> rs.getLong("user_id"), filmId));
    }

    private void updateFilmLikes(@NonNull Film film) {
        jdbcTemplate.update(DELETE_LIKES_BY_FILM_ID_SQL, film.getId());
        Optional.ofNullable(film.getLikes()).ifPresentOrElse(
                likes -> {
                    jdbcTemplate.batchUpdate(INSERT_LIKE_BY_FILM_ID_AND_USER_ID_SQL, likes, likes.size(), (ps, id) -> {
                        ps.setLong(1, film.getId());
                        ps.setLong(2, id);
                    });
                    jdbcTemplate.update(UPDATE_FILM_RATE_WITH_VALUE_SQL, likes.size(), film.getId());
                },
                () -> jdbcTemplate.update(UPDATE_FILM_RATE_WITH_VALUE_SQL, 0, film.getId()));
    }

    private Set<Genre> getGenresByFilmId(long filmId) {
        return new HashSet<>(jdbcTemplate.query(GET_GENRES_BY_FILM_ID_SQL, (rs, rowNum) ->
                new Genre(rs.getLong("genre_id"), rs.getString("name")), filmId));
    }

    private void updateFilmGenres(@NonNull Film film) {
        jdbcTemplate.update(DELETE_GENRES_BY_FILM_ID_SQL, film.getId());
        Optional.ofNullable(film.getGenres()).ifPresent(genres -> {
            jdbcTemplate.batchUpdate(INSERT_GENRE_BY_FILM_ID_ANG_GENRE_ID_SQL, genres, genres.size(), (ps, genre) -> {
                ps.setLong(1, film.getId());
                ps.setLong(2, genre.getId());
            });
        });
    }

    public void clear() {
        jdbcTemplate.update(CLEAR_FILMS);
    }
}
