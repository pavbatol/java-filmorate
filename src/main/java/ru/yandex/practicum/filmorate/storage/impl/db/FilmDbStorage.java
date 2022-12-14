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
import ru.yandex.practicum.filmorate.model.enums.SortByType;
import ru.yandex.practicum.filmorate.model.impl.Director;
import ru.yandex.practicum.filmorate.model.impl.Film;
import ru.yandex.practicum.filmorate.model.impl.Genre;
import ru.yandex.practicum.filmorate.model.impl.MpaRating;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.yandex.practicum.filmorate.validator.impl.ValidatorManager.getNonNullObject;
import static ru.yandex.practicum.filmorate.validator.impl.ValidatorManager.validateId;

@Slf4j
@Component("filmDbStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final static String UPDATE_FILM_SQL = "update films f set "
            + "f.name = ?, f.description = ?, f.release_date = ?, f.duration = ?, f.rating_id = ? , f.rate = ? "
            + "where f.film_id = ?";

    private final static String DELETE_FILM_SQL = "delete from films f where f.film_id = ?";

    private final static String FIND_ALL_FILM_SQL = "select f.*, r.rating_id as ri, r.rating as rt, r.description as dc "
            + "from films f "
            + "left join mpa_ratings r on f.rating_id = r.rating_id";

    private final static String FIND_FILM_BY_ID_SQL = "select f.*, r.rating_id as ri, r.rating as rt, r.description as dc "
            + "from films f "
            + "left join mpa_ratings r on f.rating_id = r.rating_id "
            + "where film_id = ?";

    private final static String DELETE_LIKES_BY_FILM_ID_SQL = "delete from film_likes f where f.film_id = ?";

    private final static String DELETE_LIKE_BY_FILM_ID_AND_USER_ID_SQL = "delete from film_likes f where f.film_id = ? and f.user_id = ?";

    private final static String INSERT_LIKE_BY_FILM_ID_AND_USER_ID_SQL = "insert into film_likes (film_id, user_id) values(?, ?)";

    private final static String GET_LIKES_BY_FILM_ID_SQL = "select user_id from film_likes f where f.film_id = ?";

    private final static String INSERT_GENRE_BY_FILM_ID_ANG_GENRE_ID_SQL = "insert into film_genres (film_id, genre_id) values(?, ?)";

    private final static String INSERT_DIRECTOR_BY_FILM_ID_ANG_GENRE_ID_SQL = "insert into film_directors (film_id, director_id) values(?, ?)";

    private final static String DELETE_GENRES_BY_FILM_ID_SQL = "delete from film_genres f where f.film_id = ?";

    private final static String DELETE_DIRECTORS_BY_FILM_ID_SQL = "delete from film_directors f where f.film_id = ?";

    private final static String GET_GENRES_BY_FILM_ID_SQL = "select * from film_genres fg "
            + "left join genres g on fg.genre_id = g.genre_id "
            + "where fg.film_id = ?";

    private final static String GET_DIRECTORS_BY_FILM_ID_SQL = "select * from film_directors fd "
            + "left join directors d on fd.director_id = d.director_id "
            + "where fd.film_id = ?";

    private final static String UPDATE_FILM_RATE_WITH_VALUE_SQL = "update films f set rate = ? where f.film_id = ?";

    private final static String UPDATE_FILM_RATE_WITH_INCREMENT_SQL = "update films f set rate = rate + 1 where f.film_id = ?";
    private final static String UPDATE_FILM_RATE_WITH_DECREMENT_SQL = "update films f set rate = rate - 1 where f.film_id = ?";

    private final static String CLEAR_FILMS = "delete from films";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film add(@NonNull Film film) {
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
        updateFilmDirectors(film);
        return getNonNullObject(this, film.getId());
    }

    @Override
    public Film update(@NonNull Film film) {
        validateId(this, film, null);
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
        updateFilmDirectors(film);
        return getNonNullObject(this, film.getId());
    }

    @Override
    public Film remove(Long id) {
        Film film = getNonNullObject(this, id);
        if (jdbcTemplate.update(DELETE_FILM_SQL, id) > 0) {
            return film;
        }
        throw new RuntimeException("???? ?????????????? ?????????????? ??????????");
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
    public boolean contains(Long id) {
        final  String sql = "select 1 from films f where f.film_id = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, id);
        return sqlRowSet.first();
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
    public List<Film> findPopularFilms(int count, @NonNull Long genreId, int year) {
        final String whereGenre = genreId == -1 ? null : " fg.genre_id = ?1 ";
        final String whereYear = year == -1 ? null : " extract(year from  f.release_date) = ?2 ";

        String where = Stream.of(whereGenre, whereYear)
                .filter(Objects::nonNull)
                .collect(Collectors.joining(" and "));
        where = where.length() > 0 ? "where " + where : where;

        final String sql =
                "select f.*, r.rating_id ri, r.rating rt, r.description dc " +
                "from films f " +
                "    left join mpa_ratings r on f.rating_id = r.rating_id " +
                "    left join film_genres fg on f.film_id = fg.film_id " +
                "" + where +
                "group by f.film_id " +
                "order by f.rate desc " +
                "limit ?3";

        return jdbcTemplate.query(sql, this::mapRowToFilm, genreId, year, count);
    }

    @Override
    public List<Film> findByDirectorIdWithSort(Long directorId, @NonNull List<SortByType> sortTypes) {
        class pass {
            public String sortTypeToFieldName(SortByType sortByType) {
                switch (sortByType) {
                    case YEAR: return "f.release_date";
                    case LIKES: return "ct";
                    default: throw new IllegalArgumentException(String.format("???? ???????????????????????? ?????? %s: %s",
                                SortByType.class.getSimpleName(), sortTypes));
                }
            }
        }

        final String fieldsForSort = sortTypes.stream()
                .distinct()
                .map(sortByType -> new pass().sortTypeToFieldName(sortByType))
                .collect(Collectors.joining(","));

        final String sql =
                "select f.*, r.rating_id ri, r.rating rt, r.description dc, count(fl.USER_ID) ct " +
                "from films f " +
                "   join film_directors fd on f.film_id = fd.film_id " +
                "   left join mpa_ratings r on f.rating_id = r.rating_id " +
                "   left join film_likes fl on f.film_id = fl.film_id " +
                "where fd.director_id = ? " +
                "group by f.FILM_ID, fl.FILM_ID " +
                "order by " +  fieldsForSort;

        return jdbcTemplate.query(sql, this::mapRowToFilm, directorId);
    }

    @Override
    public List<Film> findRecommendedFilms(Long userId) {
        final int usersLimit = 3;
        final String sql =
                "select f2.*, r.rating_id  ri, r.rating  rt, r.description  dc " +
                "from films f " +
                "   join film_likes fl on f.film_id = fl.film_id and fl.user_id = ?1 " +
                "   join film_likes fl2 on fl.film_id = fl2.film_id and fl2.user_id <> ?1 " +
                "   join film_likes fl3 on fl2.user_id = fl3.user_id and fl3.user_id <> ?1 " +
                "   left join film_likes fl4 on fl3.film_id = fl4.film_id and fl4.user_id = ?1 " +
                "   join films f2 on fl3.film_id = f2.film_id " +
                "   left join mpa_ratings r on f2.rating_id = r.rating_id " +
                "where fl4.user_id is null " +
                "group by f2.film_id " +
                "order by count(fl2.user_id) desc " +
                "limit ?2";
        return jdbcTemplate.query(sql, this::mapRowToFilm, userId, usersLimit);
    }

    @Override
    public List<Film> findBySearch(@NonNull String query, @NonNull List<String> searchParams) {
        final String title = "title";
        final String director = "director";
        final String fName = " lower(f.name) like '%' || ?1 || '%' ";
        final String dName = " lower(d.name) like '%' || ?1 || '%' ";
        final String directorsJoin = " left join directors d on d.director_id = fd.director_id ";
        final String where = (searchParams.isEmpty() ? List.of(title, director) : searchParams).stream()
                .filter(s -> s.equals(title) || s.equals(director))
                .distinct()
                .map(s -> s.equals(title) ? fName : dName)
                .collect(Collectors.joining(" or ")).lines()
                        .filter(s -> !s.isBlank())
                        .map(s -> s.contains(dName) ? directorsJoin + " where " + s : " where " + s)
                        .collect(Collectors.joining(" "));

        final String sql =
                "select f.*, r.rating_id ri, r.rating rt, r.description dc " +
                "from films f " +
                "   left join film_directors fd on f.film_id = fd.film_id " +
                "   left join mpa_ratings r on f.rating_id = r.rating_id " +
                "" + where +
                "group by f.film_id " +
                "order by f.rate desc";

        return jdbcTemplate.query(sql, this::mapRowToFilm, query.toLowerCase());
    }

    @Override
    public List<Film> findCommon(Long userId, Long friendId) {
        final String sql =
            "select f.*, r.rating_id ri, r.rating rt, r.description dc " +
            "from films f " +
            "    join film_likes fl on f.film_id = fl.film_id and fl.user_id = ?1 " +
            "    join film_likes fl2 on fl.film_id = fl2.film_id and fl2.user_id = ?2 " +
            "    left join mpa_ratings r on f.rating_id = r.rating_id " +
            "order by f.rate desc";
        return jdbcTemplate.query(sql, this::mapRowToFilm, userId, friendId);
    }

    public Film mapRowToFilm(ResultSet rs, int rowNum) throws SQLException {
        long filmId = rs.getLong("film_id");
        return Film.builder()
                .id(filmId)
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getLong("duration"))
                .likes(getLikesByFilmId(filmId))
                .genres(getGenresByFilmId(filmId))
                .directors(getDirectorsByFilmId(filmId))
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

    private Set<Director> getDirectorsByFilmId(long filmId) {
        return new HashSet<>(jdbcTemplate.query(GET_DIRECTORS_BY_FILM_ID_SQL, (rs, rowNum) ->
                new Director(rs.getLong("director_id"), rs.getString("name")), filmId));
    }

    private void updateFilmDirectors(@NonNull Film film) {
        jdbcTemplate.update(DELETE_DIRECTORS_BY_FILM_ID_SQL, film.getId());
        Optional.ofNullable(film.getDirectors()).ifPresent(directors -> {
            jdbcTemplate.batchUpdate(INSERT_DIRECTOR_BY_FILM_ID_ANG_GENRE_ID_SQL, directors, directors.size(), (ps, director) -> {
                ps.setLong(1, film.getId());
                ps.setLong(2, director.getId());
            });
        });
    }

    public void clear() {
        jdbcTemplate.update(CLEAR_FILMS);
    }
}
