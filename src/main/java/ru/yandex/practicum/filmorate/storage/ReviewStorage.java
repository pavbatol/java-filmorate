package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.impl.Review;

import javax.validation.constraints.Positive;
import java.util.List;

public interface ReviewStorage extends Storage<Review> {

    List<Review> findByFilmId(long filmId, @Positive int count);

    void likeReview(long reviewId, long userId) ;

     void dislikeReview(long reviewId, long userId) ;

     void removeLike(long reviewId, long userId);

     void removeDislike(long reviewId, long userId);
}
