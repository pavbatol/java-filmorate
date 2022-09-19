package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.abstracts.AbstractInMemoryStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;

import java.util.Collection;

@Slf4j
@Component
@RequiredArgsConstructor
public class InMemoryFilmStorage
        extends AbstractInMemoryStorage<Film>
        implements FilmStorage {

    @Override
    public Film add(Film film) {
        film.setId(generateId());
        storage.put(film.getId(), film);
        log.debug("Добавлен фильм {}", film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (!storage.containsKey(film.getId())) {
            String message = "Такого id нет: " + film.getId();
            log.error(message);
            throw new NotFoundException(message);
        }
        storage.put(film.getId(), film);
        log.debug("Обновлен фильм {}", film);
        return film;
    }

    @Override
    public Collection<Film> findAll() {
        log.debug("Текущее количество фильмов: {}", storage.size());
        return storage.values();
    }


}
