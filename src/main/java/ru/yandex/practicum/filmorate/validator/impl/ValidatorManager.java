package ru.yandex.practicum.filmorate.validator.impl;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Entity;
import ru.yandex.practicum.filmorate.model.impl.Film;
import ru.yandex.practicum.filmorate.model.impl.Genre;
import ru.yandex.practicum.filmorate.model.impl.MpaRating;
import ru.yandex.practicum.filmorate.model.impl.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import javax.annotation.Nullable;
import java.util.Objects;

@Slf4j
public final class ValidatorManager {
    private ValidatorManager() {
    }

    public static <T extends Entity> void validateEntity(T t) {
        Class<? extends Entity> clazz = t.getClass();
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

    public static void validateId(@NonNull Storage<?> storage, Long id, boolean checkedFoNull) {
        if (checkedFoNull && Objects.isNull(id)) {
            throw new RuntimeException(String.format("id не должен быть %s", id));
        }
        validateId(storage, id);
    }

    public static void validateId(@NonNull Storage<?> storage, @NonNull Entity entity, @Nullable String message) {
        if (!storage.contains(entity.getId())) {
            if (Objects.isNull(message) || message.isBlank()) {
                message = String.format("Такого id для %s нет: %s", entity.getClass().getSimpleName(), entity.getId());
            }
            log.error(message);
            throw new NotFoundException(message);
        }
    }

    @NonNull
    public static <T> T getNonNullObject(@NonNull Storage<T> storage, Long id) throws NotFoundException {
        return storage.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Объект по id %s не найден", id)));
    }
}
