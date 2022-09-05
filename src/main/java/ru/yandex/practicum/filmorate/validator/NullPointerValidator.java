package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

public class NullPointerValidator implements UserValidator, FilmValidator{
    @Override
    public void validate(Film film) throws ValidateException {
        if (film == null) {
            throw new ValidateException("Полученный объект Film не инициализирован");
        }
    }

    @Override
    public void validate(User user) throws ValidateException {
        if (user == null) {
            throw new ValidateException("Полученный объект User не инициализирован");
        }
    }
}
