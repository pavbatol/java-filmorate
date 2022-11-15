package ru.yandex.practicum.filmorate.service;

import lombok.NonNull;

import java.util.List;

public interface Service<T> {

    T add(T t);

    T update(T t);

    T remove(Long id);

    List<T> findAll();

    T findById(Long id);
}
