package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.impl.Genre;
import ru.yandex.practicum.filmorate.service.impl.GenreService;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService service;

    @PostMapping
    public Genre add(@Valid @RequestBody Genre genre) {
        return service.add(genre);
    }

    @PutMapping
    public Genre update(@Valid @RequestBody Genre genre) {
        return service.update(genre);
    }

    @GetMapping
    public List<Genre> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Genre findById(@PathVariable(value = "id") Long id) {
        return service.findById(id);
    }
}
