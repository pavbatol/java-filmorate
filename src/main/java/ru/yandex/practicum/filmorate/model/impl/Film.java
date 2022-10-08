package ru.yandex.practicum.filmorate.model.impl;

import lombok.*;
import lombok.experimental.NonFinal;
import lombok.experimental.SuperBuilder;
import ru.yandex.practicum.filmorate.model.directory.Genre;
import ru.yandex.practicum.filmorate.validator.annotationed.FromToNow;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.DECEMBER;

@Value
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class Film extends AbstractEntity{

    @NonNull
    @NotBlank
    String name;

    @NonNull
    @Size(max = 200)
    String description;

    @NonNull
    @FromToNow(year = 1895, month = DECEMBER, dayOfMonth = 28)
    LocalDate releaseDate;

    @Positive
    long duration;

    @NonFinal
    @Setter
    Set<Long> likes;

    @NonFinal
    @Setter
    Set<Genre> genres;

    @NonFinal
    @Setter
    Integer mpa_rating;
}
