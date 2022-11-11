package ru.yandex.practicum.filmorate.storage.impl.db;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.impl.Director;
import ru.yandex.practicum.filmorate.model.impl.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import javax.validation.constraints.Positive;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static ru.yandex.practicum.filmorate.validator.impl.ValidatorManager.getNonNullObject;
import static ru.yandex.practicum.filmorate.validator.impl.ValidatorManager.validateId;

@Validated
@Repository
@RequiredArgsConstructor
public class ReviewDbStorage implements ReviewStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;


    @Override
    public Review add(@NonNull Review review) {
        validateId(filmStorage, review.getFilmId(), true);
        validateId(userStorage, review.getUserId(), true);
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reviews")
                .usingGeneratedKeyColumns("review_id");
        Map<String, Object> values = new HashMap<>();
        values.put("content", review.getContent());
        values.put("is_positive", review.getIsPositive());
        values.put("useful", 0);
        values.put("user_id", review.getUserId());
        values.put("film_id", review.getFilmId());
        review.setId(simpleJdbcInsert.executeAndReturnKey(values).intValue());
        return review;
    }

    @Override
    public Review update(@NonNull Review review) {
        validateId(this, review, null);
        final String UPDATE_SQL = "update reviews r set r.content = ?, r.is_positive = ? where r.review_id = ?";
        jdbcTemplate.update(UPDATE_SQL, review.getContent(), review.getIsPositive(), review.getId());
        return getNonNullObject(this, review.getId());
    }

    @Override
    public Review remove(Long id) {
        Review review = getNonNullObject(this, id);
        final String REMOVE_SQL = "delete from reviews r where r.review_id = ?";
        jdbcTemplate.update(REMOVE_SQL, id);
        return review;
    }

    @Override
    public List<Review> findAll() {
        String FIND_ALL_SQL = "select * from reviews";
        return jdbcTemplate.query(FIND_ALL_SQL, this::mapRowToGenre);
    }

    @Override
    public Optional<Review> findById(Long id) {
        try {
            String FIND_BY_ID_SQL = "select * from reviews r where r.review_id = ?";
            return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_BY_ID_SQL, this::mapRowToGenre, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Review> findByFilmId(long filmId, @Positive int count) {
        String sWhere = filmId == -1 ? " where r.film_id like ? " : " where r.film_id = ? ";
        String FIND_BY_FILM_ID_SQL = String.format("select * from reviews r %s limit ?", sWhere);
        return jdbcTemplate.query(FIND_BY_FILM_ID_SQL, this::mapRowToGenre, filmId == -1 ? "%" : filmId, count);
    }

    @Override
    public void likeReview(long reviewId, long userId) {

    }

    @Override
    public void dislikeReview(long reviewId, long userId) {

    }

    @Override
    public void removeLike(long reviewId, long userId) {

    }

    @Override
    public void removeDislike(long reviewId, long userId) {

    }

    private Review mapRowToGenre(ResultSet rs, int rowNum) throws SQLException {
        return Review.builder()
                .id(rs.getLong("review_id"))
                .content(rs.getString("content"))
                .isPositive(rs.getBoolean("is_positive"))
                .useful(rs.getInt("useful"))
                .userId(rs.getLong("user_id"))
                .filmId(rs.getLong("film_id"))
                .build();
    }
}
