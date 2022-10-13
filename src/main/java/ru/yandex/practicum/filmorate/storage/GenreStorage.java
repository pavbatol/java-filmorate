package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.impl.Genre;

public interface GenreStorage extends Storage<Genre>{
    final static String UPDATE_GENRE_SQL = "update genres set name = ? where genre_id = ?";
    final static String FIND_ALL_GENRES_SQL = "select * from genres";
    final static String FIND_GENRE_BY_ID_SQL = "select * from genres where genre_id = ?";
}
