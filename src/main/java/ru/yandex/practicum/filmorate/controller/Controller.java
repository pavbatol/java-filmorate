package ru.yandex.practicum.filmorate.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/default")
public interface Controller<T> {

    @PostMapping
    @Operation(summary = "add")
    T add(@Valid @RequestBody T t);

    @PutMapping
    @Operation(summary = "update")
    T update(@Valid @RequestBody T t);

    @DeleteMapping("/{id}")
    @Operation(summary = "remove")
    default T remove(@PathVariable(value = "id") Long id) {
        throw new UnsupportedOperationException("Удаление не поддерживается");
    }

    @GetMapping
    @Operation(summary = "findAll")
    List<T> findAll();

    @GetMapping("/{id}")
    @Operation(summary = "findById")
    T findById(@PathVariable(value = "id") Long id);
}
