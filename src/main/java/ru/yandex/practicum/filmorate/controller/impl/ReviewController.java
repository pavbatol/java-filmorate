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

    public ReviewController(ReviewService reviewService) {
        super(reviewService);
        this.reviewService = reviewService;
    }

    @Override
    public Review remove(Long id) {
        return reviewService.remove(id);
    }

    /**
     * Заказчик захотел энд-поинт, кокторый прописан в интерфейсе для findAll(),
     * использовать для метода с другой сигнатурой - findByFilmId(filmId, count).
     * Поэтому переопределяем энд-поинт в @GetMapping и делаем 'заглушку'
     */
    @Override
    @GetMapping("/")
    @Operation(summary = "Этот энд-поинт не поддерживается")
    public List<Review> findAll() {
        throw new UnsupportedOperationException("Этот энд-поинт не поддерживается");
    }

    @GetMapping()
    @Operation(summary = "findByFilmId")
    public List<Review> findByFilmId(@RequestParam(value = "filmId", defaultValue = "-1") long filmId,
                                     @RequestParam(value = "count", defaultValue = "10") int count) {
        return reviewService.findByFilmId(filmId, count);
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
    @Operation(summary = "removeLike")
    public void removeLike(@PathVariable(value = "id") long reviewId,
                           @PathVariable(value = "userId") long userId) {
        reviewService.removeLike(reviewId, userId);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    @Operation(summary = "removeDislike")
    public void removeDislike(@PathVariable(value = "id") long reviewId,
                              @PathVariable(value = "userId") long userId) {
        reviewService.removeDislike(reviewId, userId);
    }
}
