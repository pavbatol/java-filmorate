package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.impl.Film;

import javax.validation.constraints.Positive;
import java.util.List;

public interface FilmStorage extends Storage<Film>{

    final static String UPDATE_FILM_SQL = "update films set "
            + "name = ?, description = ?, release_date = ?, duration = ?, rating_id = ? , rate = ?"
            + "where film_id = ?";

    final static String DELETE_FILM_SQL = "delete from films where film_id = ?";

    final static String DELETE_FILM_LIKES_SQL = "delete from film_likes where film_id = ?";

    final static String DELETE_FILM_GENRES_SQL = "delete from film_genres where film_id = ?";

    final static String FIND_ALL_FILM_SQL = "select * from films f "
            + "left join (select rating_id as ri, rating as rt, description as dc "
            + "from mpa_ratings) r on f.rating_id = ri ";

    final static String FIND_FILM_BY_ID_SQL = "select * from films f "
            + "left join (select rating_id as ri, rating as rt, description as dc "
            + "from mpa_ratings) r on f.rating_id = ri "
            + "where f.film_id = ?";

    final static String FIND_POPULAR_FILMS_SQL = "select * from films f "
            + "left join (select rating_id as ri, rating as rt, description as dc "
            + "from mpa_ratings) r on f.rating_id = ri "
            + "order by rate desc limit ?";

    final static String GET_LIKES_BY_FILM_ID_SQL = "select user_id from film_likes where film_id = ?";

    final static String DELETE_LIKES_SQL = "delete from film_likes where film_id = ?";

    final static String INSERT_LIKES_SQL = "insert into film_likes (film_id, user_id) values(?, ?)";

    final static String UPDATE_FILM_RATE_SQL = "update films set rate = ? where film_id = ?";

    final static String GET_GENRES_BY_FILM_ID = "select * from film_genres fg "
            + "left join genres g on fg.genre_id = g.genre_id "
            + "where fg.film_id = ?";

    final static String INSERT_FILM_GENRES_SQL = "insert into film_genres (film_id, genre_id) values(?, ?)";

    List<Film> findPopularFilms(@Positive int count);
}
