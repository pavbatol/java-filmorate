package ru.yandex.practicum.filmorate.storage.impl.db;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.impl.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.util.List;
import java.util.Optional;

@Repository
public class ReviewDbStorage implements ReviewStorage {
    @Override
    public Review add(Review review) {
        return null;
    }

    @Override
    public Review update(Review review) {
        return null;
    }

    @Override
    public Review remove(Long id) {
        return null;
    }

    @Override
    public List<Review> findAll() {
        return null;
    }

    @Override
    public Optional<Review> findById(Long id) {
        return Optional.empty();
    }
}
