package ru.yandex.practicum.filmorate.service.interfaces;

import java.util.Collection;

public interface Service<T> {

    T add(T t);

    T update(T t);

    T remove(Long id);

    Collection<T> findAll();

    T findById(Long id);
}
