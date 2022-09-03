package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.exception.ValidateDateException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class UserBirthdayValidator implements UserValidator{
    @Override
    public void validate(User user) throws ValidateException {
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidateDateException("Дата рождения не может быть в будущем");
        }
    }
}
