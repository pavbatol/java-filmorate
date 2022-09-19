package ru.yandex.practicum.filmorate.storage.abstracts;

import lombok.AccessLevel;
import lombok.Getter;
import ru.yandex.practicum.filmorate.storage.interfaces.Storage;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractInMemoryStorage<T> implements Storage<T> {

    @Getter(AccessLevel.PROTECTED)
    private Long lastId = 0L;

    @Getter(AccessLevel.PROTECTED)
    protected final Map<Long, T> storage = new HashMap<>();

    protected Long generateId() {
        return ++lastId;
    }

    protected void clear() {
        storage.clear();
    }

    @Override
    public boolean contains(Long id) {
        return storage.containsKey(id);
    }

    @Override
    public Optional<T> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public T remove(Long id) {
        return storage.remove(id);
    }
}
