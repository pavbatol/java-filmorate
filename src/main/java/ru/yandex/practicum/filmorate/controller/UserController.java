package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.yandex.practicum.filmorate.validator.UserValidator.runValidation;


@Slf4j
@Validated
@RestController
@RequestMapping("/users")
public class UserController {
    long lastId;
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public  List<User> findAll() {
        log.debug("Текущее количество пользователей: {}", users.size());
        return List.copyOf(users.values()) ;
    }

    @PostMapping
    public User add(@Valid @RequestBody User user) {
        runValidation(user);
        editName(user);
        user.setId(generateId());
        users.put(user.getId(), user);
        log.debug("Добавлен пользователь {}", user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        runValidation(user);
        editName(user);
        if (!users.containsKey(user.getId())) {
            String message = "Такого id нет: " + user.getId();
            log.error(message);
            throw new NotFoundException(message);
        }
        users.put(user.getId(), user);
        log.debug("Обновлен пользователь {}", user);
        return user;
    }

    private long generateId() {
        return ++lastId;
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
