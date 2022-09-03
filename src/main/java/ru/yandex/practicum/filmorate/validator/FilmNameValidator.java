package ru.yandex.practicum.filmorate.validator;

import lombok.NonNull;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.exception.ValidateNameException;
import ru.yandex.practicum.filmorate.model.Film;

public class FilmNameValidator implements FilmValidator{
    @Override
    public void validate(@NonNull Film film) throws ValidateException {
        if (film.getName().isBlank()) {
            throw new ValidateNameException("Имя не должно быть пустым");
        }
    }
}
