package ru.yandex.practicum.filmorate.storage.abstracts;

import lombok.AccessLevel;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractInMemoryStorage<T>  {

    @Getter(AccessLevel.PROTECTED)
    private Long lastId = 0L;

    @Getter(AccessLevel.PROTECTED)
    protected final Map<Long, T> storage = new HashMap<>();

    protected Long generateId() {
        return ++lastId;
    }

    protected void clearStorage() {
        storage.clear();
    }
}
