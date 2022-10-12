package ru.yandex.practicum.filmorate.service.impl;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.impl.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

@Service
public class GenreService extends AbstractService<Genre> {

    public GenreService(GenreStorage storage) {
        super(storage);
    }

    @Override
    protected String getGenericTypeName() {
        return "Жанр";
    }
}
