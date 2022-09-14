package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.yandex.practicum.filmorate.validator.FilmValidator.runValidation;

@Slf4j
@Validated
@RestController
@RequestMapping("/films")
public class FilmController {
    long lastId;
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public  List<Film> findAll() {
        log.debug("Текущее количество фильмов: {}", films.size());
        return List.copyOf(films.values()) ;
    }

    @PostMapping
    public Film add(@Valid @RequestBody Film film) {
        runValidation(film);
        film.setId(generateId());
        films.put(film.getId(), film);
        log.debug("Добавлен фильм {}", film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        runValidation(film);
        if (!films.containsKey(film.getId())) {
            String message = "Такого id нет: " + film.getId();
            log.error(message);
            throw new NotFoundException(message);
        }
        films.put(film.getId(), film);
        log.debug("Обновлен фильм {}", film);
        return film;
    }

    private long generateId() {
        return ++lastId;
    }

    protected void clearStorage() {
        films.clear();
    }
}

