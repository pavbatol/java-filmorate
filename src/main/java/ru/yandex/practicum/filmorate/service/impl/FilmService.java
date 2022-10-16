package ru.yandex.practicum.filmorate.service.impl;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.model.impl.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.constraints.Positive;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static ru.yandex.practicum.filmorate.validator.impl.ValidatorManager.getNonNullObject;
import static ru.yandex.practicum.filmorate.validator.impl.ValidatorManager.validateId;

@Slf4j
@Service
@Validated
public class FilmService extends AbstractService<Film> {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage storage,
                       @Qualifier("userDbStorage") UserStorage userStorage) {
        super(storage);
        this.filmStorage = storage;
        this.userStorage = userStorage;
    }

    @Override
    protected String getGenericTypeName() {
        return "Фильм";
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
        List<Film> result = filmStorage.findPopularFilms(count);
        return result;
    }

    @NonNull
    private Set<Long> getLikesKeeper(@NonNull Film film) {
        return Optional.ofNullable(film.getLikes()).orElseGet(() -> {
            Set<Long> likes = new HashSet<>();
            film.setLikes(likes);
            return film.getLikes();
        });
    }
}
