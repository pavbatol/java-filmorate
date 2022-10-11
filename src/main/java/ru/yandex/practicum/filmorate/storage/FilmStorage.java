package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.impl.Film;

import javax.validation.constraints.Positive;
import java.util.List;

public interface FilmStorage extends Storage<Film>{

    List<Film> findPopularFilms(@Positive int count);
}
