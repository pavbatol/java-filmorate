package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;
import ru.yandex.practicum.filmorate.validator.annotationed.FromCinemaDayToCurrent;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class Film {

    private long id;

    @NonNull
    @NotBlank
    private final String name;

    @NonNull
    @Size(max = 200)
    private String description;

    @NonNull
    @FromCinemaDayToCurrent()
    private final LocalDate releaseDate;

    @Positive
    private final long duration;
}
