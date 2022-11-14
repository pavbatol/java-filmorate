package ru.yandex.practicum.filmorate.service.impl;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.impl.Film;
import ru.yandex.practicum.filmorate.model.impl.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static ru.yandex.practicum.filmorate.validator.impl.ValidatorManager.getNonNullObject;
import static ru.yandex.practicum.filmorate.validator.impl.ValidatorManager.validateId;

@Slf4j
@Service
public class UserService extends AbstractService<User> {

    private final static String GENERIC_TYPE_NAME = "Пользователь";
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private final EventService eventService;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage storage,
                       @Qualifier("filmDbStorage") FilmStorage filmStorage,
                       EventService eventService) {
        super(storage);
        this.userStorage = storage;
        this.filmStorage = filmStorage;
        this.eventService = eventService;
    }

    @Override
    protected String getGenericTypeName() {
        return GENERIC_TYPE_NAME;
    }

    public User addFriend(Long userId, Long friendId) {
        validateId(userStorage, friendId);
        User user = getNonNullObject(userStorage, userId);
        if (getFriendsKeeper(user).contains(friendId)) {
            log.debug("{} #{} уже в друзьях у #{}", entityTypeName, friendId, userId);
            return user;
        }
        if (userStorage.addFriend(userId, friendId)) {
            log.debug("{} #{} добавлен в друзья к #{}", entityTypeName, friendId, userId);
            eventService.addAddedFriendEvent(userId, friendId);
        } else {
            log.debug("Не удалось добавить {} #{} в друзья к #{}", entityTypeName, friendId, userId);
        }
//        log.debug(userStorage.addFriend(userId, friendId)
//                ? String.format("%s #%s добавлен в друзья к #%s", entityTypeName, friendId, userId)
//                : String.format("Не удалось добавить %s #%s в друзья к #%s", entityTypeName, friendId, userId));
        return getNonNullObject(userStorage, userId);
    }

    public User removeFriend(Long userId, Long friendId) {
        validateId(userStorage, friendId);
        User user = getNonNullObject(userStorage, userId);
        if (!getFriendsKeeper(user).contains(friendId)) {
            log.debug("{} #{} не было в друзьях у #{}", entityTypeName, friendId, userId);
            return user;
        }
        if (userStorage.removeFriend(userId, friendId)) {
            log.debug("{} #{} удален из друзей у #{}", entityTypeName, friendId, userId);
            eventService.addRemovedFriendEvent(userId, friendId);
        } else {
            log.debug("Не удалось удалить {} #{} из друзей #{}", entityTypeName, friendId, userId);
        }
//        log.debug(userStorage.removeFriend(userId, friendId)
//                ? String.format("%s #%s удален из друзей у #%s", entityTypeName, friendId, userId)
//                : String.format("Не удалось удалить %s #%s из друзей #%s", entityTypeName, friendId, userId));
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

    public List<Film> findRecommendedFilms(Long userId) {
        validateId(userStorage, userId);
        List<Film> films = filmStorage.findRecommendedFilms(userId);
        log.debug("Рекомендовано для пользователя #{} {} фильмов", userId, films.size());
        return films;
    }

    @NonNull
    private Set<Long> getFriendsKeeper(@NonNull User user) {
        return Optional.ofNullable(user.getFriends()).orElseGet(() -> {
            Set<Long> friends = new HashSet<>();
            user.setFriends(friends);
            return user.getFriends();
        });
    }
}
