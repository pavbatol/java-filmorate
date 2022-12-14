package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.enums.SortByType;
import ru.yandex.practicum.filmorate.model.impl.Film;

import javax.validation.constraints.Positive;
import java.util.List;

public interface FilmStorage extends Storage<Film> {

    boolean addLike(Long filmId, Long userId);

    boolean removeLike(Long filmId, Long userId);

    List<Film> findPopularFilms(@Positive int count, Long genreId, int year);

    List<Film> findByDirectorIdWithSort(Long directorId, List<SortByType> sortParams);

    List<Film> findRecommendedFilms(Long userId);

    List<Film> findBySearch(String query, List<String> searchParams);

    List<Film> findCommon(Long userId, Long friendId);
}
