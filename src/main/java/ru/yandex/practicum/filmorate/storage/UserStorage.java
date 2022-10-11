package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.impl.User;

import java.util.List;

public interface UserStorage extends Storage<User> {

    List<User> findFriends(Long userId);

    List<User> findMutualFriends(Long userId, Long otherId);
}
