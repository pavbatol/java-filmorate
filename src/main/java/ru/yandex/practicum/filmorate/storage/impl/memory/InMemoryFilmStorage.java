package ru.yandex.practicum.filmorate.storage.impl.memory;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.enums.SortByType;
import ru.yandex.practicum.filmorate.model.impl.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.validator.impl.ValidatorManager.getNonNullObject;

@Slf4j
@Component("inMemoryFilmStorage")
public class InMemoryFilmStorage
        extends AbstractInMemoryStorage<Film>
        implements FilmStorage {

    private final static String GENERIC_TYPE_NAME = "Фильм";

    @Override
    protected String getGenericTypeName() {
        return GENERIC_TYPE_NAME;
    }

    @Override
    public boolean addLike(Long filmId, Long userId) {
        return getLikesKeeper(getNonNullObject(this, filmId)).add(userId);
    }

    @Override
    public boolean removeLike(Long filmId, Long userId) {
        return getLikesKeeper(getNonNullObject(this, filmId)).remove(userId);
    }

    @Override
    public List<Film> findPopularFilms(int count) {
        List<Film> result = this.findAll().stream()
                .sorted(this::filmCompare)
                .limit(count)
                .collect(Collectors.toList());
        log.debug("Найдено {} для {} из запрошенных {} с наибольшим количеством лайков",
                result.size(), entityTypeName, count);
        return result;
    }

    @Override
    public List<Film> findByDirectorIdWithSort(Long directorId, @NonNull List<SortByType> sorts) {
        throw new UnsupportedOperationException("Метод в секции 'memory' не поддерживается");
    }

    @Override
    public List<Film> findRecommendedFilms(Long userId) {
        throw new UnsupportedOperationException("Метод в секции 'memory' не поддерживается");
    }

    @Override
    public List<Film> findBySearch(String query, List<String> searchParams) {
        throw new UnsupportedOperationException("Метод в секции 'memory' не поддерживается");
    }

    @NonNull
    private Set<Long> getLikesKeeper(@NonNull Film film) {
        return Optional.ofNullable(film.getLikes()).orElseGet(() -> {
            Set<Long> likes = new HashSet<>();
            film.setLikes(likes);
            return film.getLikes();
        });
    }

    public int filmCompare(@NonNull Film f1, @NonNull Film f2) {
        int likes1 = f1.getLikes() == null ? 0 : f1.getLikes().size();
        int likes2 = f2.getLikes() == null ? 0 : f2.getLikes().size();
        return likes2 - likes1;
    }
}
