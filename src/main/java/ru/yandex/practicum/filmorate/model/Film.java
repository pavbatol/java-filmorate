package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;
import ru.yandex.practicum.filmorate.validator.annotationed.FromToNow;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

import static java.time.Month.DECEMBER;

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
    @FromToNow(year = 1895, month = DECEMBER, dayOfMonth = 28)
    private final LocalDate releaseDate;

    @Positive
    private final long duration;
}
