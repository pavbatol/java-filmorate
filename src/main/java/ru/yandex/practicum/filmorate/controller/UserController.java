package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.impl.UserService;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import javax.validation.Valid;
import java.util.List;

import static ru.yandex.practicum.filmorate.validator.impl.ValidatorManager.*;

@Validated
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserStorage storage;
    private final UserService service;

    @PostMapping
    public User add(@Valid @RequestBody User user) {
        validateUser(user);
        editUserName(user);
        return service.add(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        validateUser(user);
        editUserName(user);
        return service.update(user);
    }

    @GetMapping
    public List<User> findAll() {
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

    @DeleteMapping("/{id}/friends/{friendId}")
    public User removeFriend(@PathVariable(value = "id") Long userId,
                             @PathVariable(value = "friendId") Long friendId) {
        validateId(storage, userId);
        validateId(storage, friendId);
        return service.removeFriend(userId, friendId);
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
