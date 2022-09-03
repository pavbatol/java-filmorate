package ru.yandex.practicum.filmorate.validator;

import lombok.NonNull;
import ru.yandex.practicum.filmorate.exception.ValidateEmailException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserEmailValidator implements UserValidator{
    @Override
    public void validate(@NonNull User user) throws ValidateException {
        // TODO: 03.09.2022 Удалить закоменченное после проверки
//        if (user.getEmail() == null
//                || !user.getEmail().contains("@")
//                || user.getEmail().replace("@", "").isBlank()) {
//            throw new ValidateEmailException("Некорректный email");
//        }

        Pattern pattern = Pattern.compile("\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*\\.\\w{2,4}");
        Matcher matcher = pattern.matcher(user.getEmail());
        boolean matches = matcher.matches();
        if (!matches) {
            throw new ValidateEmailException("Некорректный email");
        }
    }
}
