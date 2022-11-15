package ru.yandex.practicum.filmorate.service.impl;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.impl.Director;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

@Service
public class DirectorService extends AbstractService<Director> {

    private final static String GENERIC_TYPE_NAME = "Режиссер";

    public DirectorService(DirectorStorage storage) {
        super(storage);
    }

    @Override
    protected String getGenericTypeName() {
        return GENERIC_TYPE_NAME;
    }
}
