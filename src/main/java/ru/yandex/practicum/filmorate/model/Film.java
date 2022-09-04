package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;
import ru.yandex.practicum.filmorate.validator.annotationed.PastCinemaBirthday;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class Film {

    private int id;

    @NonNull
    @NotBlank
    private final String name;

    @Pattern(regexp = "^.{0,200}$", message = "Допустимо не более 200 символов")
    private String description;

    @NonNull
    @PastCinemaBirthday(message = "Дата релиза — не раньше 28 декабря 1895 года")
    private final LocalDate releaseDate;

    @Positive
    private final long duration;
}
