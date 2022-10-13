package ru.yandex.practicum.filmorate.storage.impl.memory;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.impl.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.validator.impl.ValidatorManager.getNonNullObject;

@Slf4j
@Component("inMemoryUserStorage")
@RequiredArgsConstructor
public class InMemoryUserStorage
        extends AbstractInMemoryStorage<User>
        implements UserStorage {

    @Override
    protected String getGenericTypeName() {
        return "Пользователь";
    }

    @Override
    public List<User> findFriends(Long userId) {
        User user = getNonNullObject(this, userId);
        List<User> result = Objects.isNull(user.getFriends())
                ? List.of()
                : user.getFriends().stream()
                        .map(this::findById)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList());
        log.debug("Найдено {} друзей у {} #{}", result.size(), entityTypeName, userId);
        return result;
    }

    @Override
    public List<User> findMutualFriends(Long userId, Long otherId) {
        User user = getNonNullObject(this, userId);
        User otherUser = getNonNullObject(this, otherId);
        List<User> result = isAnyNull(user.getFriends(), otherUser.getFriends())
                ? List.of()
                : user.getFriends().stream()
                        .filter(otherUser.getFriends()::contains)
                        .map(this::findById)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList());
        log.debug("Найдено {} общих друзей у {} #{} и #{}", result.size(), entityTypeName,  userId, otherId);
        return result;
    }

    @Override
    public boolean addFriend(Long userId, Long friendId) {
        return getFriendsKeeper(getNonNullObject(this, userId)).add(friendId);
    }

    @Override
    public boolean removeFriend(Long userId, Long friendId) {
        return getFriendsKeeper(getNonNullObject(this, userId)).remove(friendId);
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
