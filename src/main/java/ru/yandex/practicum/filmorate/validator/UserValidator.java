package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.exception.ValidateDateException;
import ru.yandex.practicum.filmorate.exception.ValidateEmailException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.exception.ValidateLoginException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public final class UserValidator {
    private UserValidator() {
    }

    public static void validate(User user) throws ValidateException {
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
}
