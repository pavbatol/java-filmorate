package ru.yandex.practicum.filmorate.service.impl;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    protected String getGenericTypeName() {
        return "Фильм";
    }

    public Film addLike(Long filmId, Long userId) {
        validateId(userStorage, userId);
        Film film = getNonNullObject(filmStorage, filmId);
        log.debug(Optional.of(getLikesKeeper(film))
                .filter(likes -> likes.add(userId))
                .isPresent()
                        ? String.format("%s #%s получил лайк от пользователя #%s", entityTypeName, filmId, userId)
                        : String.format("%s #%s уже имеет лайк от пользователя #%s", entityTypeName, filmId, userId));
        return film;
    }

    public Film removeLike(Long filmId, Long userId) {
        validateId(userStorage, userId);
        Film film = getNonNullObject(filmStorage, filmId);
        log.debug(Optional.of(getLikesKeeper(film))
                .filter(likes -> likes.remove(userId))
                .isPresent()
                        ? String.format("%s #%s потерял лайк от пользователя #%s", entityTypeName, filmId, userId)
                        : String.format("%s #%s не имел лайк от пользователя #%s", entityTypeName, filmId, userId));
        return film;
    }

    public List<Film> findPopularFilms(@Positive int count) {
        List<Film> result = filmStorage.findAll().stream()
                .sorted(this::filmCompare)
                .limit(count)
                .collect(Collectors.toList());
        log.debug("Найдено {} для {} из запрошенных {} с наибольшим количеством лайков",
                result.size(), entityTypeName, count);
        return result;
    }

    @NonNull
    private Set<Long> getLikesKeeper(@NonNull Film film) {
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
