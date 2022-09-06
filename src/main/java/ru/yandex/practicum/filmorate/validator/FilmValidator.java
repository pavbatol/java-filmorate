package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static java.time.Month.DECEMBER;

public final class FilmValidator {
    private FilmValidator() {
    }

    public static void validate(Film film) throws ValidateException {
        if (film == null) {
            throw new ValidateException("Полученный объект Film не инициализирован");
        }
        if (film.getName().isBlank()) {
            throw new ValidateNameException("Имя не должно быть пустым");
        }
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            throw new ValidateDescriptionException("Максимальная длина описания 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, DECEMBER, 28))
                || film.getReleaseDate().isAfter(LocalDate.now())) {
            throw new ValidateDateException("Дата релиза — не раньше 28 декабря 1895 года и не в будущем");
        }
        if (film.getDuration() <= 0) {
            throw new ValidateDurationException("Продолжительность фильма должна быть положительной");
        }
    }
}
