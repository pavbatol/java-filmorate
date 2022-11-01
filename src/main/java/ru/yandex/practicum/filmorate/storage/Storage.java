package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

import static ru.yandex.practicum.filmorate.validator.impl.ValidatorManager.getNonNullObject;

public interface Storage<T> {

    T add(T t);

    T update(T t);

    default T remove(Long id) {
        throw new UnsupportedOperationException("Удаление не поддерживается");
    }

    List<T> findAll();

    Optional<T> findById(Long id);

    default boolean contains(Long id) {
        try {
            getNonNullObject(this, id);
            return true;
        } catch (NotFoundException e) {
            return false;
        }
    }
}
