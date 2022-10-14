package ru.yandex.practicum.filmorate.service.impl;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.impl.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

import static ru.yandex.practicum.filmorate.validator.impl.ValidatorManager.getNonNullObject;
import static ru.yandex.practicum.filmorate.validator.impl.ValidatorManager.validateId;

@Slf4j
@Service
public class UserService extends AbstractService<User> {

    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage storage) {
        super(storage);
        this.userStorage = storage;
    }

    @Override
    protected String getGenericTypeName() {
        return "Пользователь";
    }

    public User addFriend(Long userId, Long friendId) {
        validateId(userStorage, friendId);
        User user = getNonNullObject(userStorage, userId);
        if (getFriendsKeeper(user).contains(friendId)) {
            log.debug(String.format("%s #%s уже в друзьях у #%s", entityTypeName,  friendId, userId));
            return user;
        }
        log.debug(userStorage.addFriend(userId, friendId)
                ? String.format("%s #%s добавлен в друзья к #%s", entityTypeName, friendId, userId)
                : String.format("Не удалось добавить %s #%s в друзья к #%s", entityTypeName, friendId, userId));
        return getNonNullObject(userStorage, userId);
    }

    public User removeFriend(Long userId, Long friendId) {
        validateId(userStorage, friendId);
        User user = getNonNullObject(userStorage, userId);
        if (!getFriendsKeeper(user).contains(friendId)) {
            log.debug(String.format("%s #%s не было в друзьях у #%s", entityTypeName,  friendId, userId));
            return user;
        }
        log.debug(userStorage.removeFriend(userId, friendId)
                ? String.format("%s #%s удален из друзей у #%s", entityTypeName,  friendId, userId)
                : String.format("Не удалось удалить %s #%s из друзей #%s", entityTypeName, friendId, userId));
        return getNonNullObject(userStorage, userId);
    }

    public List<User> findFriends(Long userId) {
        validateId(userStorage, userId);
        return userStorage.findFriends(userId);
    }

    public List<User> findMutualFriends(Long userId, Long otherId) {
        validateId(userStorage, userId);
        validateId(userStorage, otherId);
        return userStorage.findMutualFriends(userId, otherId);
    }

    @NonNull
    private Set<Long> getFriendsKeeper(@NonNull User user) {
        return Optional.ofNullable(user.getFriends()).orElseGet(() -> {
                    Set<Long> friends = new HashSet<>();
                    user.setFriends(friends);
                    return user.getFriends();});
    }
}
