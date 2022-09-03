package ru.yandex.practicum.filmorate.validator;

import lombok.NonNull;
import ru.yandex.practicum.filmorate.model.User;

public class UserNameValidator implements UserValidator{
    @Override
    public void validate(@NonNull User user) {
        fillName(user);
    }

    public void fillName(@NonNull User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
