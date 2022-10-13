package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/default")
public interface Controller<T> {

    @PostMapping
    T add(@Valid @RequestBody T t);

    @PutMapping
    T update(@Valid @RequestBody T t);

    @DeleteMapping("/{id}")
    default T remove(@PathVariable(value = "id") Long id) {
        throw new UnsupportedOperationException("Удаление не поддерживается");
    }

    @GetMapping
    List<T> findAll();

    @GetMapping("/{id}")
    T findById(@PathVariable(value = "id") Long id);
}
