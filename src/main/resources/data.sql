insert into genres (name) select 'Комедия' where 'Комедия' not in (select name from genres);
insert into genres (name) select 'Драма' where 'Драма' not in (select name from genres);
insert into genres (name) select 'Мультфильм' where 'Мультфильм' not in (select name from genres);
insert into genres (name) select 'Триллер' where 'Триллер' not in (select name from genres);
insert into genres (name) select 'Документальный' where 'Документальный' not in (select name from genres);
insert into genres (name) select 'Боевик' where 'Боевик' not in (select name from genres);

insert INTO mpa_ratings (rating, description) select 'G', 'У фильма нет возрастных ограничений'
    where 'G' not in (select rating from mpa_ratings);
insert INTO mpa_ratings (rating, description) select 'PG', 'Детям рекомендуется смотреть фильм с родителями'
    where 'PG' not in (select rating from mpa_ratings);
insert INTO mpa_ratings (rating, description) select 'PG-13', 'Детям до 13 лет просмотр не желателен'
    where 'PG-13' not in (select rating from mpa_ratings);
insert INTO mpa_ratings (rating, description) select 'R', 'Лицам до 17 лет просматривать фильм можно только в присутствии взрослого'
    where 'R' not in (select rating from mpa_ratings);
insert INTO mpa_ratings (rating, description) select 'NC-17', 'Лицам до 18 лет просмотр запрещён'
    where 'NC-17' not in (select rating from mpa_ratings);
