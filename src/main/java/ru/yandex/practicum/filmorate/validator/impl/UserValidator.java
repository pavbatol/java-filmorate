package ru.yandex.practicum.filmorate.validator.impl;

import lombok.NonNull;
import ru.yandex.practicum.filmorate.exception.ValidateDateException;
import ru.yandex.practicum.filmorate.exception.ValidateEmailException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.exception.ValidateLoginException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.AbsractValidator;

import java.time.LocalDate;

public class UserValidator<T> extends AbsractValidator<User> {
    @Override
    public void validate(User user) throws ValidateException {
        if (user == null) {
            throw new ValidateException("Полученный объект User не инициализирован");
        }
        if (user.getEmail() == null
                || !user.getEmail().matches("\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*\\.\\w{2,4}")) {
            throw new ValidateEmailException("Некорректный email");
        }
        if (user.getLogin() == null || user.getLogin().isBlank()
                || user.getLogin().contains(" ")) {
            throw new ValidateLoginException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getBirthday() == null
                || user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidateDateException("Дата рождения не может быть в будущем");
        }
    }

    public static void editName(@NonNull User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
