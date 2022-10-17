package ru.yandex.practicum.filmorate.service.impl;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.impl.MpaRating;
import ru.yandex.practicum.filmorate.storage.MpaRatingStorage;

@Service
public class MpaRatingService extends AbstractService<MpaRating> {

    private final static String GENERIC_TYPE_NAME = "MPA-рейтинг";

    public MpaRatingService(MpaRatingStorage storage) {
        super(storage);
    }

    @Override
    protected String getGenericTypeName() {
        return GENERIC_TYPE_NAME;
    }
}
