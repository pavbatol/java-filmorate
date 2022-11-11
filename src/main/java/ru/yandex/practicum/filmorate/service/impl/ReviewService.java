package ru.yandex.practicum.filmorate.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.impl.Review;
import ru.yandex.practicum.filmorate.storage.impl.db.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.impl.db.ReviewDbStorage;

import java.util.List;

import static ru.yandex.practicum.filmorate.validator.impl.ValidatorManager.validateId;

@Service
public class ReviewService extends AbstractService<Review> {

    private final ReviewDbStorage reviewStorage;
    private final FilmDbStorage filmStorage;
    private final static String GENERIC_TYPE_NAME = "Отзыв";

    @Autowired
    public ReviewService(ReviewDbStorage storage, FilmDbStorage filmStorage) {
        super(storage);
        this.reviewStorage = storage;
        this.filmStorage = filmStorage;
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

    }

    public void dislikeReview(long reviewId, long userId) {

    }

    public void removeLike(long reviewId, long userId) {

    }

    public void removeDislike(long reviewId, long userId) {

    }
}
