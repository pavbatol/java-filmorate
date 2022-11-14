package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.impl.Event;

import java.util.List;

public interface EventStorage extends Storage<Event> {

    List<Event> findByUserId(int userId);

}
