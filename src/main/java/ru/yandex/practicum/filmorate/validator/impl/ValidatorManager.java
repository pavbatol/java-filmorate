package ru.yandex.practicum.filmorate.validator.impl;

import lombok.NonNull;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.impl.Film;
import ru.yandex.practicum.filmorate.model.impl.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import static ru.yandex.practicum.filmorate.validator.impl.UserValidator.editName;

public final class ValidatorManager {
    private ValidatorManager() {
    }

    public static void validateEntity(Film obj) {
        new FilmValidator<Film>().runValidation(obj);
    }

    public static void validateEntity(User obj) {
       new UserValidator<User>().runValidation(obj);
    }

    public static void editUserName(@NonNull User user) {
        editName(user);
    }

    public static void validateId(@NonNull Storage<?> storage, Long id) {
        if (!storage.contains(id)) {
            throw new NotFoundException(String.format("id %s не найден", id));
        }
    }

    @NonNull
    public static <T> T getNonNullObject(@NonNull Storage<T> storage, Long id) throws NotFoundException{
        return storage.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(String.format("Объект по id %s не найден", id)));
    }
}
