package ru.yandex.practicum.filmorate.validator;

import lombok.NonNull;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.exception.ValidateLoginException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserLoginValidator implements UserValidator{
    @Override
    public void validate(@NonNull User user) throws ValidateException {
        Pattern whitespace = Pattern.compile("\\s");
        Matcher matcher = whitespace.matcher(user.getLogin());
        if (user.getLogin().isBlank() || matcher.find()) {
            throw new ValidateLoginException("Логин не может быть пустым и содержать пробелы");
        }
    }
}
