package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Validated
@RestController
@RequestMapping("/films")
public class FilmController {
    long lastId;
    private final Map<Long, Film> films = new HashMap<>();

    @PostMapping
    public Film add(@Valid @RequestBody Film film) {
        runValidation(film);
        film.setId(generateId());
        films.put(film.getId(), film);
        log.info("Добавлен фильм {}", film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        runValidation(film);
        if (!films.containsKey(film.getId())) {
            log.warn("Такого id нет: {}", film.getId());
            throw new ValidateException("Такого id нет:" + film.getId());
        }
        films.put(film.getId(), film);
        log.info("Обновлен фильм {}", film);
        return film;
    }

    @GetMapping
    public  List<Film> findAll() {
        log.info("Текущее количество фильмов: {}", films.size());
        return List.copyOf(films.values()) ;
    }

    private long generateId() {
        return ++lastId;
    }

    private void runValidation(Film film) throws ValidateException {
        try {
            FilmValidator.validate(film);
        } catch (ValidateException e) {
            log.warn("Валидация полей для Film не пройдена: " + e.getMessage());
            throw e;
        }
    }

    protected void clearStorage() {
        films.clear();
    }
}

