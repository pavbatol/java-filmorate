package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.impl.MpaRating;

public interface MpaRatingStorage extends Storage<MpaRating> {
    final static String UPDATE_MPARATING_SQL = "update mpa_ratings set rating = ?, description = ? where rating_id = ?";
    final static String FIND_ALL_MPARATINGS_SQL = "select * from mpa_ratings";
    final static String FIND_MPARATING_BY_ID_SQL = "select * from mpa_ratings where rating_id = ?";
}
