package ru.yandex.practicum.filmorate.controller.impl;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.impl.Film;
import ru.yandex.practicum.filmorate.service.impl.FilmService;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.List;

@Validated
@RestController
@RequestMapping("/films")
public class FilmController extends AbstractController<Film, FilmService> {

    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        super(filmService);
        this.filmService = filmService;
    }

    @Override
    public Film remove(Long id) {
        return filmService.remove(id);
    }

    @PutMapping("/{id}/like/{userId}")
    @Operation(summary = "addLike")
    public Film addLike(@PathVariable(value = "id") Long filmId,
                        @PathVariable(value = "userId") Long userId) {
        return filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @Operation(summary = "removeLike")
    public Film removeLike(@PathVariable(value = "id") Long filmId,
                           @PathVariable(value = "userId") Long userId) {
        return filmService.removeLike(filmId, userId);
    }

    @GetMapping("/popular")
    @Operation(summary = "findPopularFilms")
    public List<Film> findPopularFilms(
            @RequestParam(value = "count", defaultValue = "10", required = false) @Positive int count,
            @RequestParam(value = "genreId", required = false, defaultValue = "-1") Long genreId,
            @RequestParam(value = "year", required = false, defaultValue = "-1") int year) {
        return filmService.findPopularFilms(count, genreId, year);
    }

    @GetMapping("/director/{directorId}")
    @Operation(summary = "findByDirectorIdWithSort")
    public List<Film> findByDirectorIdWithSort(@PathVariable(value = "directorId") Long dirId,
                                               @RequestParam(value = "sortBy", required = false) List<String> sortParams) {
        return filmService.findByDirectorIdWithSort(dirId, sortParams);
    }

    @GetMapping("/search")
    @Operation(summary = "findBySearch")
    public List<Film> findBySearch(@RequestParam(value = "query") @NotBlank String query,
                                   @RequestParam(value = "by", required = false) List<String> searchParams) {
        return filmService.findBySearch(query, searchParams);
    }

    @GetMapping("/common")
    @Operation(summary = "findCommon")
    public List<Film> findCommon(@RequestParam(value = "userId") Long userId,
                                 @RequestParam(value = "friendId") Long friendId) {
        return filmService.findCommon(userId, friendId);
    }
}

