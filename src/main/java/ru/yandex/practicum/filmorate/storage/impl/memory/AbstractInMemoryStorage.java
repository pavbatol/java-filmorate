package ru.yandex.practicum.filmorate.storage.impl.memory;

import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Entity;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
public abstract class AbstractInMemoryStorage<T extends Entity> implements Storage<T> {

    @Getter
    private long lastId = 0;
    protected final Map<Long, T> container = new HashMap<>();
    protected final String entityTypeName = getGenericTypeName();

    protected abstract String getGenericTypeName();

    private long generateId() {
        return ++lastId;
    }

    @Override
    public T add(@NonNull T t) {
        t.setId(generateId());
        if (contains(t.getId())) {
            String message = String.format("Такой id для %s уже есть: %s", t.getClass().getSimpleName(), t.getId());
            log.error(message);
            throw new AlreadyExistsException(message);
        }
        container.put(t.getId(), t);
        return t;
    }

    @Override
    public T update(@NonNull T t) {
        if (!contains(t.getId())) {
            String message = String.format("Такого id для %s нет: %s", t.getClass().getSimpleName(), t.getId());
            log.error(message);
            throw new NotFoundException(message);
        }
        container.put(t.getId(), t);
        return t;
    }

    @Override
    public T remove(Long id) {
        return container.remove(id);
    }

    @Override
    public List<T> findAll() {
        return List.copyOf(container.values());
    }

    @Override
    public Optional<T> findById(Long id) {
        return Optional.ofNullable(container.get(id));
    }

    @Override
    public boolean contains(Long id) {
        return container.containsKey(id);
    }

    protected void clear() {
        container.clear();
    }
}
