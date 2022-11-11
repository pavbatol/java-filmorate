package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.impl.Review;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

import static ru.yandex.practicum.filmorate.validator.impl.ValidatorManager.validateId;

@Slf4j
@Service
public class ReviewService extends AbstractService<Review> {

    private final static String GENERIC_TYPE_NAME = "Отзыв";
    private final ReviewStorage reviewStorage;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public ReviewService(ReviewStorage reviewStorage,
                         @Qualifier("filmDbStorage") FilmStorage filmStorage,
                         @Qualifier("userDbStorage") UserStorage userStorage) {
        super(reviewStorage);
        this.reviewStorage = reviewStorage;
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @Override
    protected String getGenericTypeName() {
        return GENERIC_TYPE_NAME;
    }

    public List<Review> findByFilmId(long filmId, int count) {
        if (filmId != -1) {
            validateId(filmStorage, filmId);
        }
        return reviewStorage.findByFilmId(filmId, count);
    }

    public void likeReview(long reviewId, long userId) {
        validateId(reviewStorage, reviewId);
        validateId(userStorage, userId);
        reviewStorage.likeReview(reviewId, userId);
        log.debug("Добавлен лайк к отзыву #reviewId пользователем #userId");
    }

    public void dislikeReview(long reviewId, long userId) {
        validateId(reviewStorage, reviewId);
        validateId(userStorage, userId);
        reviewStorage.dislikeReview(reviewId, userId);
        log.debug("Добавлен дизлайк к отзыву #reviewId пользователем #userId");
    }

    public void removeLike(long reviewId, long userId) {
        validateId(reviewStorage, reviewId);
        validateId(userStorage, userId);
        reviewStorage.removeLike(reviewId, userId);
        log.debug("Удален лайк к отзыву #reviewId пользователем #userId");
    }

    public void removeDislike(long reviewId, long userId) {
        validateId(reviewStorage, reviewId);
        validateId(userStorage, userId);
        reviewStorage.removeDislike(reviewId, userId);
        log.debug("Удален дизлайк к отзыву #reviewId пользователем #userId");
    }
}
