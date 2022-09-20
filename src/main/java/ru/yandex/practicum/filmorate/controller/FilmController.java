package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.impl.FilmService;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

import static ru.yandex.practicum.filmorate.validator.FilmValidator.runValidation;
import static ru.yandex.practicum.filmorate.validator.common.CommonValidator.validateId;
import static ru.yandex.practicum.filmorate.validator.common.CommonValidator.validatePositive;

@Slf4j
@Validated
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService service;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @PostMapping
    public Film add(@Valid @RequestBody Film film) {
        runValidation(film);
        return service.add(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        runValidation(film);
        return service.update(film);
    }

    @GetMapping
    public Collection<Film> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Film findById(@PathVariable(value = "id") Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable(value = "id") Long filmId,
                          @PathVariable(value = "userId") Long userId) {
        validateId(userStorage, userId);
        validateId(filmStorage, userId);
        return service.addLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film removeLike(@PathVariable(value = "id") Long filmId,
                             @PathVariable(value = "userId") Long userId) {
        validateId(userStorage, userId);
        validateId(filmStorage, userId);
        return service.removeLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> findPopularFilms(@RequestParam(defaultValue = "10", required = false) int count) {
        validatePositive(count, "'count' не должен быть отрицательным");
        return service.findPopularFilms(count);
    }
}

