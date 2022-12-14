package ru.yandex.practicum.filmorate.validator.impl;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Entity;
import ru.yandex.practicum.filmorate.model.impl.*;
import ru.yandex.practicum.filmorate.storage.Storage;

import javax.annotation.Nullable;
import java.util.Objects;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ValidatorManager {

    public static <T extends Entity> void validateEntity(T t) {
        if (t instanceof Film) {
            new FilmValidator().runValidation((Film) t);
        } else if (t instanceof User) {
            new UserValidator().runValidation((User) t);
        } else if (t instanceof Review) {
            new ReviewValidator().runValidation((Review) t);
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
