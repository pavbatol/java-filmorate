package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Validated
@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    int lastId;
    private final Map<Integer, User> users = new HashMap<>();

    @PostMapping
    public User add(@Valid  @RequestBody User user) throws ValidateException {
        runValidation(user);
        user.setId(generateId());
        users.put(user.getId(), user);
        log.debug("Добавлен пользователь {}", user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) throws ValidateException {
        runValidation(user);
        if (!users.containsKey(user.getId())) {
            log.debug("Такого id нет: {}", user.getId());
            throw new ValidateException("Такого id нет:" + user.getId());
        }
        users.put(user.getId(), user);
        log.debug("Обновлен пользователь {}", user);
        return user;
    }

    @GetMapping
    public  List<User> findAll() {
        log.debug("Текущее количество пользователей: {}", users.size());
        return List.copyOf(users.values()) ;
    }

    private int generateId() {
        return ++lastId;
    }

    private List<UserValidator> getValidators() {
        return List.of(
                new NullPointerValidator(),
                new UserLoginValidator(),
                new UserNameValidator(),
                new UserEmailValidator(),
                new UserBirthdayValidator()
        );
    }

    private void runValidation(User user) throws ValidateException {
        for (UserValidator validator : getValidators()) {
            try {
                validator.validate(user);
            } catch (ValidateException e) {
                log.debug("Валидация полей для User не пройдена: " + e.getMessage());
                throw e;
            }
        }
    }
}
