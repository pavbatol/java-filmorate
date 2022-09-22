package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.exception.EntityValidation.ValidateException;

public interface Validator<T> {

    void validate(T t) throws ValidateException;

    default void runValidation(T t) throws ValidateException {
        try {
            validate(t);
        } catch (ValidateException e) {
            System.out.printf("Валидация полей для %s не пройдена: %s",
                    t.getClass().getSimpleName(),  e.getMessage());
            throw e;
        }
    }
}
