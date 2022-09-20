package ru.yandex.practicum.filmorate.service.impl;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.abstracts.AbstractService;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.validator.common.CommonValidator.validateId;
import static ru.yandex.practicum.filmorate.validator.common.CommonValidator.validatePositive;

@Slf4j
@Service
public class FilmService extends AbstractService<Film> {

    FilmStorage filmStorage;
    UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage storage, UserStorage userStorage) {
        super(storage);
        this.filmStorage = storage;
        this.userStorage = userStorage;
    }

    public Film addLike(Long filmId, Long userId) {
        validateId(userStorage, userId);
        Film film = validateId(filmStorage, filmId);
        log.debug(Optional.of(getWithSetLikesKeeper(film))
                .filter(l -> l.add(userId))
                .isPresent()
                        ? String.format("Фильм #%s получил лайк от пользователя #%s", filmId, userId)
                        : String.format("Фильм #%s уже имеет лайк от пользователя #%s", filmId, userId));
        return film;
    }

    public Film removeLike(Long filmId, Long userId) {
        validateId(userStorage, userId);
        Film film = validateId(filmStorage, filmId);
        log.debug(Optional.of(getWithSetLikesKeeper(film))
                .filter(l -> l.remove(userId))
                .isPresent()
                        ? String.format("Фильм #%s потерял лайк от пользователя #%s", filmId, userId)
                        : String.format("Фильм #%s не имел лайк от пользователя #%s", filmId, userId));
        return film;
    }

    public List<Film> findPopularFilms(int count) {
        validatePositive(count, "'count' не должен быть отрицательным");
        List<Film> result = filmStorage.findAll().stream()
                .sorted(this::filmCompare)
                .limit(count)
                .collect(Collectors.toList());
        log.debug("Найдено {} из запрошенных {} фильмов с наибольшим количеством лайков",
                result.size(), count);
        return result;
    }

    private Set<Long> getWithSetLikesKeeper(@NonNull Film film) {
        return Optional.ofNullable(film.getLikes()).orElseGet(() -> {
            Set<Long> likes = new HashSet<>();
            film.setLikes(likes);
            return film.getLikes();});
    }

    public int filmCompare(@NonNull Film f1, @NonNull Film f2) {
        int likes1 = f1.getLikes() == null ? 0 : f1.getLikes().size();
        int likes2 = f2.getLikes() == null ? 0 : f2.getLikes().size();
        return likes2 - likes1;
    }
}
