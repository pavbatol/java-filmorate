package ru.yandex.practicum.filmorate.storage.impl.memory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.impl.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

@Slf4j
@Component
@RequiredArgsConstructor
public class InMemoryFilmStorage
        extends AbstractInMemoryStorage<Film>
        implements FilmStorage {
}
