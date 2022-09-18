package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.impl.InMemoryFilmStorage;

import javax.validation.Valid;
import java.util.Collection;

import static ru.yandex.practicum.filmorate.validator.FilmValidator.runValidation;

@Slf4j
@Validated
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final InMemoryFilmStorage storage;

    @GetMapping
    public Collection<Film> findAll() {
        return storage.findAll();
    }

    @PostMapping
    public Film add(@Valid @RequestBody Film film) {
        runValidation(film);
        return storage.add(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        runValidation(film);
        return storage.update(film);
    }
}

