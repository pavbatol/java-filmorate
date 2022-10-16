package ru.yandex.practicum.filmorate.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Entity;
import ru.yandex.practicum.filmorate.service.Service;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.List;

import static ru.yandex.practicum.filmorate.validator.impl.ValidatorManager.getNonNullObject;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractService<T extends Entity> implements Service<T> {

    private final Storage<T> storage;

    protected final String entityTypeName = getGenericTypeName();

    protected abstract String getGenericTypeName();

    @Override
    public T add(@NonNull T t) {
        T added = storage.add(t);
        log.debug("Добавлен {}: {}", entityTypeName, added);
        return added;
    }

    @Override
    public T update(T t) {
        T updated = storage.update(t);
        log.debug("Обновлен {}: {}", entityTypeName, updated);
        return updated;
    }

    @Override
    public T remove(Long id) {
        T removed = storage.remove(id);
        log.debug("Обновлен {}: {}", entityTypeName, removed);
        return removed;
    }

    @Override
    public List<T> findAll() {
        List<T> found = storage.findAll();
        log.debug("Текущий размер списка для {}: {}", entityTypeName, found.size());
        return found;
    }

    @Override
    public T findById(Long id) {
        T found = getNonNullObject(storage, id);
        log.debug("Найден {}: {}", entityTypeName, found);
        return found;
    }
}
