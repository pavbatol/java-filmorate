# java-filmorate
`Сервис поиска фильмов для просмотра`

### Схемы базы данных
![ER-diagram](filmorate.png)

#### Пример запроса - получить все фильмы
```SQL
select *
from films
```
#### Пример запроса - получить топ 10 наиболее популярных фильмов
```SQL
SELECT f.name
FROM films f
JOIN (
    SELECT film_id
    FROM film_likes
    GROUP BY film_id
    ORDER BY COUNT(user_id) DESC
    LIMIT 10) t ON t.film_id = f.film_id   
```
#### Пример запроса - получить всех пользователей
```SQL
select *
from users
```
#### Пример запроса - список общих друзей с другим пользователем
```SQL
--Имея id двух пользователей: me_id и other_id

SELECT u.user_id, u.name
FROM user u
JOIN (
    SELECT f.fr_Id 
    FROM (
        SELECT friend_id AS fr_Id
        FROM friends  
        WHERE me_id = user_id AND confirmed IS TRUE
    ) f
    JOIN (
        SELECT friend_id AS fr_Id
        FROM friends  
        WHERE other_id = user_id AND confirmed IS TRUE
    ) t ON t.fr_Id = f.fr_Id
) mutual ON u.user_id = mutual.fr_Id 
```