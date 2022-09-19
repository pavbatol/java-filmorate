package ru.yandex.practicum.filmorate.storage.interfaces;

import java.util.Collection;
import java.util.Optional;

public interface Storage<T> {

    T add(T t);

    T update(T t);

    T remove(Long id);

    Collection<T> findAll();

    Optional<T> findById(Long id);

    boolean contains(Long id);

}
