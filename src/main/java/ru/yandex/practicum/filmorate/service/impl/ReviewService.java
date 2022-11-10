package ru.yandex.practicum.filmorate.service.impl;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.impl.Review;
import ru.yandex.practicum.filmorate.storage.impl.db.ReviewDbStorage;

import java.util.List;

@Service
public class ReviewService extends AbstractService<Review> {

    private final static String GENERIC_TYPE_NAME = "Отзыв";

    public ReviewService(ReviewDbStorage storage) {
        super(storage);
    }

    @Override
    protected String getGenericTypeName() {
        return GENERIC_TYPE_NAME;
    }

    public List<Review> findReviewsByFilmId(long filmId, int count) {
        return null;
    }

    public void likeReview(long reviewId, long userId) {

    }

    public void dislikeReview(long reviewId, long userId) {

    }

    public void deleteLike(long reviewId, long userId) {

    }

    public void deleteDislike(long reviewId, long userId) {

    }
}
