package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;

public interface FilmValidator {
    void validate(Film film) throws ValidateException;
}
