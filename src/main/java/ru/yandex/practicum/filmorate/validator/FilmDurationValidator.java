package ru.yandex.practicum.filmorate.validator;

import lombok.NonNull;
import ru.yandex.practicum.filmorate.exception.ValidateDurationException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;

public class FilmDurationValidator implements FilmValidator{
    @Override
    public void validate(@NonNull Film film) throws ValidateException {
        if (film.getDuration() <= 0) {
            throw new ValidateDurationException("Продолжительность фильма должна быть положительной");
        }
    }
}
