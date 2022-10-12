package ru.yandex.practicum.filmorate.controller.impl;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.impl.MpaRating;
import ru.yandex.practicum.filmorate.service.impl.MpaRatingService;

@Validated
@RestController
@RequestMapping("/mpa")
public class MpaRatingController extends AbstractController<MpaRating, MpaRatingService> {

    public MpaRatingController(MpaRatingService service) {
        super(service);
    }
}
