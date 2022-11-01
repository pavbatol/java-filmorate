package ru.yandex.practicum.filmorate.service.impl;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.impl.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

@Service
public class GenreService extends AbstractService<Genre> {

    private final static String GENERIC_TYPE_NAME = "Жанр";

    public GenreService(GenreStorage storage) {
        super(storage);
    }

    @Override
    protected String getGenericTypeName() {
        return GENERIC_TYPE_NAME;
    }
}
