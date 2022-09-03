package ru.yandex.practicum.filmorate.validator;

import lombok.NonNull;
import ru.yandex.practicum.filmorate.exception.ValidateDateException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

public class FilmDateValidator implements FilmValidator{
    @Override
    public void validate(@NonNull Film film) throws ValidateException {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidateDateException("Дата релиза — не раньше 28 декабря 1895 года");
        }
    }
}
