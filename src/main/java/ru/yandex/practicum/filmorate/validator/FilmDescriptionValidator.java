package ru.yandex.practicum.filmorate.validator;

import lombok.NonNull;
import ru.yandex.practicum.filmorate.exception.ValidateDescriptionException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;

public class FilmDescriptionValidator implements FilmValidator{
    @Override
    public void validate(@NonNull Film film) throws ValidateException {
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            throw new ValidateDescriptionException("Максимальная длина описания 200 символов");
        }
    }
}
