package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.abstracts.AbstractInMemoryStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.Collection;

@Slf4j
@Component
public class InMemoryUserStorage
        extends AbstractInMemoryStorage<User>
        implements UserStorage {

    @Override
    public User add(User user) {
        user.setId(generateId());
        storage.put(user.getId(), user);
        log.debug("Добавлен пользователь {}", user);
        return user;
    }

    @Override
    public User update(User user) {
        if (!storage.containsKey(user.getId())) {
            String message = "Такого id нет: " + user.getId();
            log.error(message);
            throw new NotFoundException(message);
        }
        storage.put(user.getId(), user);
        log.debug("Обновлен пользователь {}", user);
        return user;
    }

    @Override
    public Collection<User> findAll() {
        log.debug("Текущее количество пользователей: {}", storage.size());
        return storage.values();
    }


}
