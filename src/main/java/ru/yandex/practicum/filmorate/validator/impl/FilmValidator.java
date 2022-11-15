package ru.yandex.practicum.filmorate.validator.impl;

import ru.yandex.practicum.filmorate.exception.EntityValidation.*;
import ru.yandex.practicum.filmorate.model.impl.Film;

import java.time.LocalDate;

import static java.time.Month.DECEMBER;

public class FilmValidator extends AbstractValidator<Film> {
    @Override
    public void validate(Film film) throws ValidateException {
        if (film == null) {
            throw new ValidateException("Полученный объект Film не инициализирован");
        }
        if (film.getName().isBlank()) {
            throw new ValidateNameException("Имя не должно быть пустым");
        }
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            throw new ValidateDescriptionException("Максимальная длина описания 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, DECEMBER, 28))) {
            throw new ValidateDateException("Дата релиза — не раньше 28 декабря 1895");
        }
        if (film.getDuration() <= 0) {
            throw new ValidateDurationException("Продолжительность фильма должна быть положительной");
        }
    }
}
