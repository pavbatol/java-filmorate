package ru.yandex.practicum.filmorate.service.impl;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.abstracts.AbstractService;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.validator.common.CommonValidator.validateId;

@Slf4j
@Service
public class UserService extends AbstractService<User> {

    UserStorage userStorage;

    @Autowired
    public UserService(UserStorage storage) {
        super(storage);
        this.userStorage = storage;
    }

    public User addFriend(Long userId, Long friendId) {
        User user = validateId(userStorage, userId);
        User friend = validateId(userStorage, friendId);
        log.debug(Optional.of(getWithSetFriendsKeeper(user))
                .filter(f -> f.add(friendId))
                .isPresent()
                        ? String.format("Пользователь #%s добавлен в друзья к #%s", friendId, userId)
                        : String.format("Пользователь #%s уже в друзьях у #%s", friendId, userId));
        getWithSetFriendsKeeper(friend).add(userId);
        return friend;
    }

    public User removeFriend(Long userId, Long friendId) {
        User user = validateId(userStorage, userId);
        User friend = validateId(userStorage, friendId);
        log.debug(Optional.of(getWithSetFriendsKeeper(user))
                .filter(f -> f.remove(friendId))
                .isPresent()
                        ? String.format("Пользователь #%s удален из друзей у #%s", friendId, userId)
                        : String.format("Пользователя #%s не было в друзьях у #%s", friendId, userId));
        return friend;
    }

    public List<User> findFriends(Long userId) {
        User user = validateId(userStorage, userId);
        List<User> result = Objects.isNull(user.getFriends())
                ? List.of()
                : user.getFriends().stream()
                        .map(this::findById)
                        .collect(Collectors.toList());
        log.debug("Найдено {} друзей у пользователей #{}", result.size(), userId);
        return result;
    }

    public List<User> findMutualFriends(Long userId, Long friendId) {
        User user = validateId(userStorage, userId);
        User friend = validateId(userStorage, friendId);
        List<User> result = isNull(user.getFriends(), friend.getFriends())
                ? List.of()
                : user.getFriends().stream()
                        .filter(friend.getFriends()::contains)
                        .map(this::findById)
                        .collect(Collectors.toList());
        log.debug("Найдено {} общих друзей у пользователей #{} и #{}", result.size(), userId, friendId);
        return result;
    }

    private Set<Long> getWithSetFriendsKeeper(@NonNull User user) {
        return Optional.ofNullable(user.getFriends()).orElseGet(() -> {
                    Set<Long> friends = new HashSet<>();
                    user.setFriends(friends);
                    return user.getFriends();});
    }

    private boolean isNull(Object... o) {
        return Arrays.stream(o).anyMatch(Objects::isNull);
    }
}
