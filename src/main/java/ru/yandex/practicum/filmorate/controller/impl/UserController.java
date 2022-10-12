package ru.yandex.practicum.filmorate.controller.impl;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.impl.User;
import ru.yandex.practicum.filmorate.service.impl.UserService;

import java.util.List;

@Validated
@RestController
@RequestMapping("/users")
public class UserController extends AbstractController<User, UserService> {

    public UserController(UserService service) {
        super(service);
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
