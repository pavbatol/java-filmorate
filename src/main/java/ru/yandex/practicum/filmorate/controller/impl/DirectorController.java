package ru.yandex.practicum.filmorate.controller.impl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.impl.Director;
import ru.yandex.practicum.filmorate.service.impl.DirectorService;

@RestController
@RequestMapping("/directors")
public class DirectorController extends AbstractController<Director, DirectorService> {

    private final DirectorService directorService;

    public DirectorController(DirectorService service) {
        super(service);
        this.directorService = service;
    }

    @Override
    public Director remove(Long id) {
        return directorService.remove(id);
    }
}
