# java-filmorate
`Сервис поиска фильмов для просмотра`

### Схемы базы данных
![ER-diagram](filmorate.png)

#### Пример запроса - получить все фильмы
```SQL
select f.name
from films f
```
#### Пример запроса - получить топ 10 наиболее популярных фильмов
```SQL    
select f.name 
from films f 
order by rate desc limit 10
```
#### Пример запроса - получить всех пользователей
```SQL
select *
from users
```
#### Пример запроса - список общих друзей с другим пользователем
```SQL
--Имея id двух пользователей: USERID и OTHERID

select u.* from friends f
join friends f2 on f.friend_id = f2.friend_id
join users u on f.friend_id = u.user_id
where f.user_id = USERID and f2.user_id = OTHERID 
```

#### Пример запроса - список друзей пользователя
```SQL
--Имея id пользователя: USERID

select u.* from friends f 
join users u on f.friend_id = u.user_id 
where f.user_id = USERID
```

#### Пример запроса - список друзей пользователя с дополнительным полем подтвержен-неподтвержден
```SQL
--Имея id пользователя: USERID

select u.*, case when f2.friend_id is not null then true else false end confirmed 
from friends f 
left join friends f2 on f.friend_id = f2.user_id and f.user_id = USERID and f2.friend_id = USERID 
join users u on f.friend_id = u.user_id 
where f.user_id  = USERID
```

#### Пример запроса - список подтвержденных друзей пользователя
```SQL
--Имея id пользователя: USERID

select u.* from friends f 
join friends f2 on f.friend_id = f2.user_id 
join users u on f.friend_id = u.user_id 
where f.user_id = USERID and f2.friend_id = USERID
```

#### Пример запроса - список НЕподтвержденных друзей пользователя
```SQL
--Имея id пользователя: USERID

select u.* from friends f
left join friends f2 on f.friend_id = f2.user_id and f.user_id = USERID and f2.friend_id = USERID
join users u on f.friend_id = u.user_id
where f.user_id  = USERID and f2.friend_id is null
```