package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.impl.User;

import java.util.List;

public interface UserStorage extends Storage<User> {

    boolean addFriend(Long userId, Long friendId);

    boolean removeFriend(Long userId, Long friendId);

    List<User> findFriends(Long userId);

    List<User> findMutualFriends(Long userId, Long otherId);
}
