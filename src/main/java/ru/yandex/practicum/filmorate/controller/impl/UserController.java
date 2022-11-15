package ru.yandex.practicum.filmorate.controller.impl;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.impl.Event;
import ru.yandex.practicum.filmorate.model.impl.Film;
import ru.yandex.practicum.filmorate.model.impl.User;
import ru.yandex.practicum.filmorate.service.impl.EventService;
import ru.yandex.practicum.filmorate.service.impl.UserService;

import java.util.List;

@Validated
@RestController
@RequestMapping("/users")
public class UserController extends AbstractController<User, UserService> {

    private final UserService userService;
    private final EventService eventService;

    public UserController(UserService userService, EventService eventService) {
        super(userService);
        this.userService = userService;
        this.eventService = eventService;
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

    @GetMapping("/{id}/recommendations")
    @Operation(summary = "findRecommendedFilms")
    public List<Film> findRecommendedFilms(@PathVariable(value = "id") Long userId) {
        return userService.findRecommendedFilms(userId);
    }

    @GetMapping("/{id}/feed")
    public List<Event> findEvents(@PathVariable(value = "id") int userId) {
        return eventService.findByUserId(userId);
    }
}
