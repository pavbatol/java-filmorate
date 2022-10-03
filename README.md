# java-filmorate
`Сервис поиска фильмов для просмотра`

### Схемы базы данных
![ER-diagram](filmorate.png)

#### Пример запроса - получить все фильмы
```SQL
select *
from film
```
#### Пример запроса - получить топ 10 наиболее популярных фильмов
```SQL
SELECT f.name
FROM film f
JOIN (
    SELECT film_id
    FROM film_like
    GROUP BY film_id
    ORDER BY COUNT(user_id) DESC
    LIMIT 10) t ON t.film_id = f.film_id   
```
#### Пример запроса - получить всех пользователей
```SQL
select *
from user
```
#### Пример запроса - список общих друзей с другим пользователем
```SQL
--На данном этапе разработки пользователями являются друзьями друг друга.
--Соответственно отбор необходимо делать и в первом и во втором поле
--Имея id двух пользователей: me_id и other_id

SELECT u.user_id, u.name
FROM user u
JOIN (
    SELECT f.fr_Id 
    FROM (
        SELECT 
            CASE
                WHEN me_id = user_id_1 THEN user_id_2
                ELSE user_id_1
            END AS fr_Id
        FROM friend  
        WHERE me_id IN (user_id_1, user_id_2) AND confirmed IS TRUE) f
    JOIN (
        SELECT 
            CASE
                WHEN other_id = user_id_1 THEN user_id_2
                ELSE user_id_1
            END AS fr_Id
        FROM friend  
        WHERE other_id IN (user_id_1, user_id_2) AND confirmed IS TRUE
    ) t ON t.fr_Id = f.fr_Id
) mutual ON u.user_id = mutual.fr_Id 
```