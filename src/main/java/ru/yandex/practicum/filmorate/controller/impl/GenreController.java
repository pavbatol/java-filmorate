package ru.yandex.practicum.filmorate.controller.impl;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.impl.Genre;
import ru.yandex.practicum.filmorate.service.impl.GenreService;

@Validated
@RestController
@RequestMapping("/genres")
public class GenreController extends AbstractController<Genre, GenreService> {

    public GenreController(GenreService genreService) {
        super(genreService);
    }

    @Override
    public Genre add(Genre genre) {
        throw new UnsupportedOperationException("Изменение справочника пользователем сервиса не поддерживается");
    }

    @Override
    public Genre update(Genre genre) {
        throw new UnsupportedOperationException("Изменение справочника пользователем сервиса не поддерживается");
    }
}
