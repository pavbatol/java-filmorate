package ru.yandex.practicum.filmorate.validator;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.Storage;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.time.LocalDate;

@Slf4j
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

    public static void runValidation(User obj) throws ValidateException {
        try {
            validate(obj);
        } catch (ValidateException e) {
            log.warn("Валидация полей для " + obj.getClass().getSimpleName() + " не пройдена: " + e.getMessage());
            throw e;
        }
    }

    public static void editName(User user) {
        if (user == null) {
            return;
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
