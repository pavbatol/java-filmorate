package ru.yandex.practicum.filmorate.storage.impl.memory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.impl.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

@Slf4j
@Component("inMemoryUserStorage")
@RequiredArgsConstructor
public class InMemoryUserStorage
        extends AbstractInMemoryStorage<User>
        implements UserStorage {
}
