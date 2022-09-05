package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;
import ru.yandex.practicum.filmorate.validator.annotationed.FromCinemaDayToCurrent;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class Film {

    private int id;

    @NonNull
    @NotBlank
    private final String name;

    @Length(max = 200)
    private String description;

    @NonNull
    @FromCinemaDayToCurrent()
    private final LocalDate releaseDate;

    @Positive
    private final long duration;
}
