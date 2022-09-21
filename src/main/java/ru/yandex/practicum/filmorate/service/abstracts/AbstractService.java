package ru.yandex.practicum.filmorate.service.abstracts;

import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.filmorate.service.interfaces.Service;
import ru.yandex.practicum.filmorate.storage.interfaces.Storage;

import java.util.List;

import static ru.yandex.practicum.filmorate.validator.impl.ValidatorManager.getNonNullObject;

@RequiredArgsConstructor
public abstract class AbstractService<T> implements Service<T> {

    private final Storage<T> storage;

    @Override
    public T add(T t) {
        return storage.add(t);
    }

    @Override
    public T update(T t) {
        return storage.update(t);
    }

    @Override
    public T remove(Long id) {
        return storage.remove(id);
    }
    @Override
    public List<T> findAll() {
        return storage.findAll();
    }

    @Override
    public T findById(Long id) {
        return getNonNullObject(storage, id);
    }
}
