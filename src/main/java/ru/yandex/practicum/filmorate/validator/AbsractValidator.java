package ru.yandex.practicum.filmorate.validator;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidateException;

@Slf4j
public abstract class AbsractValidator<T> implements Validator<T> {

    @Override
    public void runValidation(T t) throws ValidateException {
        try {
            validate(t);
        } catch (ValidateException e) {
            log.debug("Валидация полей для {} не пройдена: {}",
                    t.getClass().getSimpleName(),  e.getMessage());
            throw e;
        }
    }
}
