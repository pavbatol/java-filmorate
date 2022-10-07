MERGE INTO genres KEY (genre_id, name) VALUES (1, 'Комедия');
MERGE INTO genres KEY (genre_id, name) VALUES (2, 'Драма');
MERGE INTO genres KEY (genre_id, name) VALUES (3, 'Мультфильм');
MERGE INTO genres KEY (genre_id, name) VALUES (4, 'Триллер');
MERGE INTO genres KEY (genre_id, name) VALUES (5, 'Документальный');
MERGE INTO genres KEY (genre_id, name) VALUES (6, 'Боевик');

MERGE INTO mpa_ratings KEY (rating_id, rating) VALUES (1, 'G', 'У фильма нет возрастных ограничений');
MERGE INTO mpa_ratings KEY (rating_id, rating) VALUES (2, 'PG', 'Детям рекомендуется смотреть фильм с родителями');
MERGE INTO mpa_ratings KEY (rating_id, rating) VALUES (3, 'PG-13', 'Детям до 13 лет просмотр не желателен');
MERGE INTO mpa_ratings KEY (rating_id, rating) VALUES (4, 'R', 'Лицам до 17 лет просматривать фильм можно только в присутствии взрослого');
MERGE INTO mpa_ratings KEY (rating_id, rating) VALUES (5, 'NC-17', 'Лицам до 18 лет просмотр запрещён');
