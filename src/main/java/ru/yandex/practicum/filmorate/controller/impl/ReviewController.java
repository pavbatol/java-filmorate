package ru.yandex.practicum.filmorate.controller.impl;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.impl.Review;
import ru.yandex.practicum.filmorate.service.impl.ReviewService;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController extends AbstractController<Review, ReviewService> {

    private final ReviewService reviewService;

    public ReviewController(ReviewService service, ReviewService reviewService) {
        super(service);
        this.reviewService = reviewService;
    }

    @Override
    public Review remove(Long id) {
        return reviewService.remove(id);
    }

    /**
     * Заказчик захотел энд-поинт, кокторый прописан в интерфейсе для findAll(),
     * использовать для метода с другой сигнатурой - findReviewsByFilmId(filmId, count).
     * Поэтому переопределяем энд-поинт в @GetMapping и делаем 'заглушку'
     */
    @Override
    @GetMapping("/")
    @Operation(summary = "Этот энд-поинт не поддерживается")
    public List<Review> findAll() {
        throw new UnsupportedOperationException("Этот энд-поинт не поддерживается");
    }

    @GetMapping()
    @Operation(summary = "findReviewsByFilmId")
    public List<Review> findReviewsByFilmId(@RequestParam(value = "filmId", defaultValue = "0") long filmId,
                                            @RequestParam(value = "count", defaultValue = "10") int count) {
        return reviewService.findReviewsByFilmId(filmId, count);
    }

    @PutMapping("/{id}/like/{userId}")
    @Operation(summary = "likeReview")
    public void likeReview(@PathVariable(value = "id") long reviewId,
                           @PathVariable(value = "userId") long userId) {
        reviewService.likeReview(reviewId, userId);
    }

    @PutMapping("/{id}/dislike/{userId}")
    @Operation(summary = "dislikeReview")
    public void dislikeReview(@PathVariable(value = "id") long reviewId,
                              @PathVariable(value = "userId") long userId) {
        reviewService.dislikeReview(reviewId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @Operation(summary = "deleteLike")
    public void deleteLike(@PathVariable(value = "id") long reviewId,
                           @PathVariable(value = "userId") long userId) {
        reviewService.deleteLike(reviewId, userId);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    @Operation(summary = "deleteDislike")
    public void deleteDislike(@PathVariable(value = "id") long reviewId,
                              @PathVariable(value = "userId") long userId) {
        reviewService.deleteDislike(reviewId, userId);
    }
}
