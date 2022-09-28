package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.impl.Film;
import ru.yandex.practicum.filmorate.service.impl.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

import static ru.yandex.practicum.filmorate.validator.impl.ValidatorManager.validateEntity;

@Validated
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService service;

    @PostMapping
    public Film add(@Valid @RequestBody Film film) {
        validateEntity(film);
        return service.add(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        validateEntity(film);
        return service.update(film);
    }

    @GetMapping
    public List<Film> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Film findById(@PathVariable(value = "id") Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable(value = "id") Long filmId,
                        @PathVariable(value = "userId") Long userId) {
        return service.addLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film removeLike(@PathVariable(value = "id") Long filmId,
                           @PathVariable(value = "userId") Long userId) {
        return service.removeLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> findPopularFilms(
            @RequestParam(defaultValue = "10", required = false)  @Positive int count) {
        return service.findPopularFilms(count);
    }
}

