package ru.yandex.practicum.filmorate.storage.impl.memory;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.impl.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component("inMemoryFilmStorage")
public class InMemoryFilmStorage
        extends AbstractInMemoryStorage<Film>
        implements FilmStorage {

    @Override
    protected String getGenericTypeName() {
        return "Фильм";
    }

    @Override
    public List<Film> findPopularFilms(int count) {
        List<Film> result = this.findAll().stream()
                .sorted(this::filmCompare)
                .limit(count)
                .collect(Collectors.toList());
        log.debug("Найдено {} для {} из запрошенных {} с наибольшим количеством лайков",
                result.size(), entityTypeName, count);
        return result;
    }

    public int filmCompare(@NonNull Film f1, @NonNull Film f2) {
        int likes1 = f1.getLikes() == null ? 0 : f1.getLikes().size();
        int likes2 = f2.getLikes() == null ? 0 : f2.getLikes().size();
        return likes2 - likes1;
    }
}
