package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    private FilmController ctrl;
    private Film film;

    @BeforeEach
    void setUp() {
        ctrl = new FilmController();
        film = new Film("fimName", LocalDate.now(), 120);
    }

    @Test
    void should_validation_passed_when_all_field_good() throws ValidateException {
        ctrl.add(film);

        assertEquals(1, ctrl.findAll().size(), "Size is not equal");
    }

    @Test
    void should_validation_not_passed_when_date_wrong() {
        final Film film2 = new Film("fimName", LocalDate.of(1895, 12, 27), 120);

        assertThrows(ValidateException.class,
                () -> ctrl.add(film2), "Date validation passed");

    }

    @Test
    void should_validation_not_passed_when_date_is_null_or_not() {
        Film film2;

        try {
            film2 = new Film("fimName", LocalDate.of(1895, 12, 27), 120);
            assertNotNull(film2);
        } catch (NullPointerException e) {
            fail("Exception thrown");
        }

        assertThrows(NullPointerException.class,
                () -> new Film("fimName", null, 120), "Exception not thrown");

    }
}