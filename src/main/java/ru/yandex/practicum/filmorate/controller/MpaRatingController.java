package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.impl.MpaRating;
import ru.yandex.practicum.filmorate.service.impl.MpaRatingService;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaRatingController {

    private final MpaRatingService service;

    @PostMapping
    public MpaRating add(@Valid @RequestBody MpaRating mpaRating) {
        return service.add(mpaRating);
    }

    @PutMapping
    public MpaRating update(@Valid @RequestBody MpaRating mpaRating) {
        return service.update(mpaRating);
    }

    @GetMapping
    public List<MpaRating> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public MpaRating findById(@PathVariable(value = "id") Long id) {
        return service.findById(id);
    }
}
