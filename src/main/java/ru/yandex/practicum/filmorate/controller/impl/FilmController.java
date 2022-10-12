package ru.yandex.practicum.filmorate.controller.impl;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.impl.Film;
import ru.yandex.practicum.filmorate.service.impl.FilmService;

import javax.validation.constraints.Positive;
import java.util.List;

@Validated
@RestController
@RequestMapping("/films")
public class FilmController extends AbstractController<Film, FilmService> {

    public FilmController(FilmService service) {
        super(service);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable(value = "id") Long filmId,
                        @PathVariable(value = "userId") Long userId) {
        return service.addLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film removeLike(@PathVariable(value = "id") Long filmId,
                           @PathVariable(value = "userId") Long userId) {
        return service.removeLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> findPopularFilms(
            @RequestParam(defaultValue = "10", required = false)  @Positive int count) {
        return service.findPopularFilms(count);
    }
}

