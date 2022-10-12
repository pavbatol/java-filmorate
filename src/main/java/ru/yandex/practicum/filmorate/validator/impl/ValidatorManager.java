package ru.yandex.practicum.filmorate.validator.impl;

import lombok.NonNull;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.impl.Film;
import ru.yandex.practicum.filmorate.model.impl.Genre;
import ru.yandex.practicum.filmorate.model.impl.MpaRating;
import ru.yandex.practicum.filmorate.model.impl.User;
import ru.yandex.practicum.filmorate.storage.Storage;

public final class ValidatorManager {
    private ValidatorManager() {
    }

    public static <T> void validateEntity(T t) {
        Class<?> clazz = t.getClass();
        if (clazz == Film.class) {
            new FilmValidator().runValidation((Film) t);
        } else if (clazz == User.class) {
            new UserValidator().runValidation((User) t);
        } else if (clazz == MpaRating.class) {
            //---
        } else if (clazz == Genre.class) {
            //---
        }
    }

    public static void validateId(@NonNull Storage<?> storage, Long id) {
        if (!storage.contains(id)) {
            throw new NotFoundException(String.format("id %s не найден", id));
        }
    }

    @NonNull
    public static <T> T getNonNullObject(@NonNull Storage<T> storage, Long id) throws NotFoundException{
        return storage.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Объект по id %s не найден", id)));
    }
}
