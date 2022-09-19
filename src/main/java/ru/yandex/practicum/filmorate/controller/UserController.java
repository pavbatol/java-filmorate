package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.impl.UserService;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

import static ru.yandex.practicum.filmorate.validator.UserValidator.editName;
import static ru.yandex.practicum.filmorate.validator.UserValidator.runValidation;
import static ru.yandex.practicum.filmorate.validator.common.CommonValidator.validateId;

@Slf4j
@Validated
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserStorage storage;
    private final UserService service;

    @PostMapping
    public User add(@Valid @RequestBody User user) {
        runValidation(user);
        editName(user);
        return service.add(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        runValidation(user);
        editName(user);
        return service.update(user);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User remove(@PathVariable(value = "id") Long userId,
                       @PathVariable(value = "friendId") Long friendId) {
        validateId(storage, userId);
        validateId(storage, friendId);
        return service.removeFriend(userId, friendId);
    }

    @GetMapping
    public Collection<User> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable(value = "id") Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable(value = "id") Long userId,
                          @PathVariable(value = "friendId") Long friendId) {
        validateId(storage, userId);
        validateId(storage, friendId);
        return service.addFriend(userId, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> findFriends(@PathVariable(value = "id") Long userId) {
        validateId(storage, userId);
        return service.findFriends(userId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> findMutualFriends(@PathVariable(value = "id") Long userId,
                                        @PathVariable(value = "otherId")Long friendId) {
        validateId(storage, userId);
        validateId(storage, friendId);
        return service.findMutualFriends(userId, friendId);
    }
}
