package ru.yandex.practicum.filmorate.controller.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.Controller;
import ru.yandex.practicum.filmorate.model.Entity;
import ru.yandex.practicum.filmorate.service.Service;

import javax.validation.Valid;
import java.util.List;

import static ru.yandex.practicum.filmorate.validator.impl.ValidatorManager.validateEntity;

@Validated
@RequiredArgsConstructor
public abstract class AbstractController<T extends Entity, S extends Service<T>> implements Controller<T> {

    protected final S service;

    @Override
    public T add(@Valid @RequestBody T t) {
        validateEntity(t);
        return service.add(t);
    }

    @Override
    public T update(@Valid @RequestBody T t) {
        validateEntity(t);
        return service.update(t);
    }

    @Override
    public List<T> findAll() {
        return service.findAll();
    }

    @Override
    public T findById(Long id) {
        return service.findById(id);
    }
}
