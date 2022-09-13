package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Validated
@RestController
@RequestMapping("/users")
public class UserController {
    long lastId;
    private final Map<Long, User> users = new HashMap<>();

    @PostMapping
    public User add(@Valid  @RequestBody User user) {
        runValidation(user);
        editName(user);
        user.setId(generateId());
        users.put(user.getId(), user);
        log.info("Добавлен пользователь {}", user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        runValidation(user);
        editName(user);
        if (!users.containsKey(user.getId())) {
            log.warn("Такого id нет: {}", user.getId());
            throw new NotFoundException("Такого id нет:" + user.getId());
        }
        users.put(user.getId(), user);
        log.info("Обновлен пользователь {}", user);
        return user;
    }

    @GetMapping
    public  List<User> findAll() {
        log.info("Текущее количество пользователей: {}", users.size());
        return List.copyOf(users.values()) ;
    }

    private long generateId() {
        return ++lastId;
    }

    private void runValidation(User user) throws ValidateException {
        try {
            UserValidator.validate(user);
        } catch (ValidateException e) {
            log.warn("Валидация полей для User не пройдена: " + e.getMessage());
            throw e;
        }
    }

    private void editName(User user) {
        if (user == null) {
            return;
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    protected void clearStorage() {
        users.clear();
    }
}
