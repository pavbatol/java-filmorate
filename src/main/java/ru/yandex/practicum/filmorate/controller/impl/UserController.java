package ru.yandex.practicum.filmorate.controller.impl;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.impl.User;
import ru.yandex.practicum.filmorate.service.impl.UserService;

import java.util.List;

@Validated
@RestController
@RequestMapping("/users")
public class UserController extends AbstractController<User, UserService> {

    private final UserService userService;

    public UserController(UserService userService) {
        super(userService);
        this.userService = userService;
    }

    @Override
    public User remove(Long id) {
        return userService.remove(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    @Operation(summary = "addFriend")
    public User addFriend(@PathVariable(value = "id") Long userId,
                          @PathVariable(value = "friendId") Long friendId) {
        return userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    @Operation(summary = "removeFriend")
    public User removeFriend(@PathVariable(value = "id") Long userId,
                             @PathVariable(value = "friendId") Long friendId) {
        return userService.removeFriend(userId, friendId);
    }

    @GetMapping("/{id}/friends")
    @Operation(summary = "findFriends")
    public List<User> findFriends(@PathVariable(value = "id") Long userId) {
        return userService.findFriends(userId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    @Operation(summary = "findMutualFriends")
    public List<User> findMutualFriends(@PathVariable(value = "id") Long userId,
                                        @PathVariable(value = "otherId") Long otherId) {
        return userService.findMutualFriends(userId, otherId);
    }
}
