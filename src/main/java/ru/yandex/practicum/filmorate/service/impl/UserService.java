package ru.yandex.practicum.filmorate.service.impl;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.impl.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.validator.impl.ValidatorManager.getNonNullObject;

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
        User user = getNonNullObject(userStorage, userId);
        User friend = getNonNullObject(userStorage, friendId);
        log.debug(Optional.of(getFriendsKeeper(user))
                .filter(friends -> friends.add(friendId))
                .isPresent()
                        ? String.format("%s #%s добавлен в друзья к #%s", entityTypeName,  friendId, userId)
                        : String.format("%s #%s уже в друзьях у #%s", entityTypeName,  friendId, userId));
        getFriendsKeeper(friend).add(userId);
        return friend;
    }

    public User removeFriend(Long userId, Long friendId) {
        User user = getNonNullObject(userStorage, userId);
        User friend = getNonNullObject(userStorage, friendId);
        log.debug(Optional.of(getFriendsKeeper(user))
                .filter(friends -> friends.remove(friendId))
                .isPresent()
                        ? String.format("%s #%s удален из друзей у #%s", entityTypeName,  friendId, userId)
                        : String.format("%s #%s не было в друзьях у #%s", entityTypeName,  friendId, userId));
        return friend;
    }

    public List<User> findFriends(Long userId) {
        User user = getNonNullObject(userStorage, userId);
        List<User> result = Objects.isNull(user.getFriends())
                ? List.of()
                : user.getFriends().stream()
                        .map(this::findById)
                        .collect(Collectors.toList());
        log.debug("Найдено {} друзей у {} #{}", result.size(), entityTypeName, userId);
        return result;
    }

    public List<User> findMutualFriends(Long userId, Long otherId) {
        User user = getNonNullObject(userStorage, userId);
        User otherUser = getNonNullObject(userStorage, otherId);
        List<User> result = isAnyNull(user.getFriends(), otherUser.getFriends())
                ? List.of()
                : user.getFriends().stream()
                        .filter(otherUser.getFriends()::contains)
                        .map(this::findById)
                        .collect(Collectors.toList());
        log.debug("Найдено {} общих друзей у {} #{} и #{}", result.size(), entityTypeName,  userId, otherId);
        return result;
    }

    @NonNull
    private Set<Long> getFriendsKeeper(@NonNull User user) {
        return Optional.ofNullable(user.getFriends()).orElseGet(() -> {
                    Set<Long> friends = new HashSet<>();
                    user.setFriends(friends);
                    return user.getFriends();});
    }

    private boolean isAnyNull(Object... o) {
        return Arrays.stream(o).anyMatch(Objects::isNull);
    }
}
