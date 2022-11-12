package ru.yandex.practicum.filmorate.service.impl;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.model.enums.SortByType;
import ru.yandex.practicum.filmorate.model.impl.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.impl.db.DirectorDbStorage;

import javax.validation.constraints.Positive;
import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.validator.impl.ValidatorManager.getNonNullObject;
import static ru.yandex.practicum.filmorate.validator.impl.ValidatorManager.validateId;

@Slf4j
@Service
@Validated
public class FilmService extends AbstractService<Film> {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final DirectorDbStorage directorStorage;
    private final static String GENERIC_TYPE_NAME = "Фильм";

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage storage,
                       @Qualifier("userDbStorage") UserStorage userStorage, DirectorDbStorage directorStorage) {
        super(storage);
        this.filmStorage = storage;
        this.userStorage = userStorage;
        this.directorStorage = directorStorage;
    }

    @Override
    protected String getGenericTypeName() {
        return GENERIC_TYPE_NAME;
    }

    public Film addLike(Long filmId, Long userId) {
        validateId(userStorage, userId);
        Film film = getNonNullObject(filmStorage, filmId);
        if (getLikesKeeper(film).contains(userId)) {
            log.debug(String.format("%s #%s уже имеет лайк от пользователя #%s", entityTypeName, filmId, userId));
            return film;
        }
        log.debug(filmStorage.addLike(filmId, userId)
                ? String.format("%s #%s получил лайк от пользователя #%s", entityTypeName, filmId, userId)
                : String.format("%s #%s Не удалось добавить лайк от пользователя #%s", entityTypeName, filmId, userId));
        return getNonNullObject(filmStorage, filmId);
    }

    public Film removeLike(Long filmId, Long userId) {
        validateId(userStorage, userId);
        Film film = getNonNullObject(filmStorage, filmId);
        if (!getLikesKeeper(film).contains(userId)) {
            log.debug(String.format("%s #%s не имел лайк от пользователя #%s", entityTypeName, filmId, userId));
            return film;
        }
        log.debug(filmStorage.removeLike(filmId, userId)
                ? String.format("%s #%s потерял лайк от пользователя #%s", entityTypeName, filmId, userId)
                : String.format("%s #%s Не удалось удалить лайк от пользователя #%s", entityTypeName, filmId, userId));
        return getNonNullObject(filmStorage, filmId);
    }

    public List<Film> findPopularFilms(@Positive int count) {
        return filmStorage.findPopularFilms(count);
    }

    public List<Film> findByDirectorIdWithSort(Long dirId, List<String> sortParams) {
        validateId(directorStorage, dirId);
        List<SortByType> sortTypes = Optional.ofNullable(sortParams)
                .map(strings -> strings.stream()
                        .map(SortByType::valueOfParam)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
        return filmStorage.findByDirectorIdWithSort(dirId, sortTypes);
    }

    @NonNull
    private Set<Long> getLikesKeeper(@NonNull Film film) {
        return Optional.ofNullable(film.getLikes()).orElseGet(() -> {
            Set<Long> likes = new HashSet<>();
            film.setLikes(likes);
            return film.getLikes();
        });
    }

    public List<Film> findBySearchWithSort(String query, List<String> sortParams) {
        return filmStorage.findBySearchWithSort(query, sortParams);
    }
}
