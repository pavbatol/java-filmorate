package ru.yandex.practicum.filmorate.service.impl;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.abstracts.AbstractService;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import javax.validation.constraints.Positive;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.validator.impl.ValidatorManager.getNonNullObject;
import static ru.yandex.practicum.filmorate.validator.impl.ValidatorManager.validateId;

@Slf4j
@Service
@Validated
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
        Film film = getNonNullObject(filmStorage, filmId);
        log.debug(Optional.of(getWithSetLikesKeeper(film))
                .filter(l -> l.add(userId))
                .isPresent()
                        ? String.format("Фильм #%s получил лайк от пользователя #%s", filmId, userId)
                        : String.format("Фильм #%s уже имеет лайк от пользователя #%s", filmId, userId));
        return film;
    }

    public Film removeLike(Long filmId, Long userId) {
        validateId(userStorage, userId);
        Film film = getNonNullObject(filmStorage, filmId);
        log.debug(Optional.of(getWithSetLikesKeeper(film))
                .filter(l -> l.remove(userId))
                .isPresent()
                        ? String.format("Фильм #%s потерял лайк от пользователя #%s", filmId, userId)
                        : String.format("Фильм #%s не имел лайк от пользователя #%s", filmId, userId));
        return film;
    }

    public List<Film> findPopularFilms(@Positive int count) {
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
