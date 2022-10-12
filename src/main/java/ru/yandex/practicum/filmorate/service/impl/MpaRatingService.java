package ru.yandex.practicum.filmorate.service.impl;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.impl.MpaRating;
import ru.yandex.practicum.filmorate.storage.MpaRatingStorage;

@Service
public class MpaRatingService extends AbstractService<MpaRating> {

    public MpaRatingService(MpaRatingStorage storage) {
        super(storage);
    }

    @Override
    protected String getGenericTypeName() {
        return "MPA-рейтинг";
    }
}
