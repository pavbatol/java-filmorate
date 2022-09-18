package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.impl.InMemoryUserStorage;

import javax.validation.Valid;
import java.util.Collection;

import static ru.yandex.practicum.filmorate.validator.UserValidator.editName;
import static ru.yandex.practicum.filmorate.validator.UserValidator.runValidation;

@Slf4j
@Validated
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final InMemoryUserStorage storage;

    @GetMapping
    public Collection<User> findAll() {
        return storage.findAll();
    }

    @PostMapping
    public User add(@Valid @RequestBody User user) {
        runValidation(user);
        editName(user);
        return storage.add(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        runValidation(user);
        editName(user);
        return storage.update(user);
    }
}
