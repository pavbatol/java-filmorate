package ru.yandex.practicum.filmorate.validator.impl;

import ru.yandex.practicum.filmorate.exception.EntityValidation.ValidateException;
import ru.yandex.practicum.filmorate.model.impl.Review;

import java.util.Objects;

public class ReviewValidator extends AbstractValidator<Review> {
    @Override
    public void validate(Review review) throws ValidateException {
        if (Objects.isNull(review)) {
            throw new ValidateException("Полученный объект Review не инициализирован");
        }
        if (review.getContent().isBlank()) {
            throw new ValidateException("Для Review поле content должно быть заполнено");
        }
        if (Objects.isNull(review.getIsPositive())) {
            throw new ValidateException("Для Review поле isPositive должно быть указано");
        }
    }
}
