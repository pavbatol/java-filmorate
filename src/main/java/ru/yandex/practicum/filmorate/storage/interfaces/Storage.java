package ru.yandex.practicum.filmorate.storage.interfaces;

import java.util.Collection;

public interface Storage<T> {

    Collection<T> findAll();

    T add(T t);

    T update(T t);
}
