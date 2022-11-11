package ru.yandex.practicum.filmorate.storage.impl.db;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.model.impl.Review;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

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

    @Qualifier("userDbStorage")
    private final UserStorage userStorage;

    @Qualifier("filmDbStorage")
    private final FilmStorage filmStorage;

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
        final String Sql = "update reviews r set r.content = ?, r.is_positive = ? where r.review_id = ?";
        jdbcTemplate.update(Sql, review.getContent(), review.getIsPositive(), review.getId());
        return getNonNullObject(this, review.getId());
    }

    @Override
    public Review remove(Long id) {
        Review review = getNonNullObject(this, id);
        final String Sql = "delete from reviews r where r.review_id = ?";
        jdbcTemplate.update(Sql, id);
        return review;
    }

    @Override
    public List<Review> findAll() {
        final String Sql = "select * from reviews r order by r.useful desc";
        return jdbcTemplate.query(Sql, this::mapRowToGenre);
    }

    @Override
    public Optional<Review> findById(Long id) {
        try {
            final String Sql = "select * from reviews r where r.review_id = ?";
            return Optional.ofNullable(jdbcTemplate.queryForObject(Sql, this::mapRowToGenre, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Review> findByFilmId(long filmId, @Positive int count) {
        final String sWhere = filmId == -1
                ? " where r.film_id like ? "
                : " where r.film_id = ? ";
        final String Sql = String.format("select * from reviews r %s order by r.useful desc limit ?", sWhere);
        return jdbcTemplate.query(Sql, this::mapRowToGenre,
                filmId == -1 ? "%" : filmId,
                count);
    }

    @Override
    public void likeReview(long reviewId, long userId) {
        markReview(reviewId, userId, true);
    }

    @Override
    public void dislikeReview(long reviewId, long userId) {
        markReview(reviewId, userId, false);
    }

    @Override
    public void removeLike(long reviewId, long userId) {
        if (removeMark(reviewId, userId)) {
            updateUseful(reviewId, -1);
        }
    }

    @Override
    public void removeDislike(long reviewId, long userId) {
        if (removeMark(reviewId, userId)) {
            updateUseful(reviewId, 1);
        }
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

    private void markReview(long reviewId, long userId, boolean liked) {
        final String Sql = "insert into review_marks (review_id, user_id, mark) " +
                "    (select ?1, ?2, ?3 where not exists( " +
                "             select 1 " +
                "             from review_marks r" +
                "             where r.review_id = ?1 " +
                "               and r.user_id = ?2 " +
                "         ))";

        if (jdbcTemplate.update(Sql, reviewId, userId, liked) == 1) {
            updateUseful(reviewId, liked ? 1 : -1);
        }
    }

    private boolean removeMark(long reviewId, long userId) {
        final String Sql = "delete from review_marks  where review_id = ? and  user_id = ?";
        return jdbcTemplate.update(Sql, reviewId, userId) == 1;
    }

    private void updateUseful(long reviewId, int addedValue) {
        final String Sql = "update reviews set useful = useful + ?1 where review_id = ?2";
        jdbcTemplate.update(Sql, addedValue, reviewId);
    }
}
