package ru.yandex.practicum.filmorate.controller;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
@Validated
public class FilmController {
    int lastId;
    private final Map<Integer, Film> films = new HashMap<>();

    @PostMapping
    public Film add(@Valid @RequestBody Film film) throws ValidateException {
        if (film == null) {
            log.debug("Получен null");
            throw new ValidateException("Получен null");
        }
//        try {
//            runValidation(film);
//        } catch (ValidateException e) {
//            log.debug("Валидация полей для Film не пройдена: " + e.getMessage());
//            throw e;
//        }
        film.setId(generateId());
        films.put(film.getId(), film);
        log.debug("Добавлен фильм {}", film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) throws ValidateException {
        if (film == null) {
            log.debug("Получен null");
            throw new ValidateException("Получен null");
        }
        try {
            runValidation(film);
        } catch (ValidateException e) {
            log.debug("Валидация полей для Film не пройдена: " + e.getMessage());
            throw e;
        }
        if (!films.containsKey(film.getId())) {
            log.debug("Такого id нет: {}", film.getId());
            throw new ValidateException("Такого id нет:" + film.getId());
        }

        films.put(film.getId(), film);
        log.debug("Обновлен фильм {}", film);
        return film;
    }

    @GetMapping
    public  List<Film> findAll() {
        log.debug("Текущее количество фильмов: {}", films.size());
        return List.copyOf(films.values()) ;
    }

    private int generateId() {
        return ++lastId;
    }

    private List<FilmValidator> getValidators() {
        return List.of(
                new FilmNameValidator(),
                new FilmDescriptionValidator(),
                new FilmDateValidator(),
                new FilmDurationValidator()
        );
    }

    private void runValidation(@NonNull Film film) throws ValidateException {
        for (FilmValidator validator : getValidators()) {
            validator.validate(film);
        }
    }
}
