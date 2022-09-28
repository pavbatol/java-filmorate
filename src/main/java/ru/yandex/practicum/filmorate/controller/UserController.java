package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.impl.User;
import ru.yandex.practicum.filmorate.service.impl.UserService;

import javax.validation.Valid;
import java.util.List;

import static ru.yandex.practicum.filmorate.validator.impl.ValidatorManager.editUserName;
import static ru.yandex.practicum.filmorate.validator.impl.ValidatorManager.validateEntity;

@Validated
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping
    public User add(@Valid @RequestBody User user) {
        validateEntity(user);
        editUserName(user);
        return service.add(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        validateEntity(user);
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
        return service.addFriend(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User removeFriend(@PathVariable(value = "id") Long userId,
                             @PathVariable(value = "friendId") Long friendId) {
        return service.removeFriend(userId, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> findFriends(@PathVariable(value = "id") Long userId) {
        return service.findFriends(userId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> findMutualFriends(@PathVariable(value = "id") Long userId,
                                        @PathVariable(value = "otherId") Long otherId) {
        return service.findMutualFriends(userId, otherId);
    }
}
