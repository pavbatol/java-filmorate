package ru.yandex.practicum.filmorate.model.impl;

import lombok.*;
import lombok.experimental.NonFinal;
import lombok.experimental.SuperBuilder;
import ru.yandex.practicum.filmorate.validator.annotationed.From;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.DECEMBER;

@Value
@ToString(callSuper = true)
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class Film extends AbstractEntity {

    @NonNull
    @NotBlank
    String name;

    @NonNull
    @Size(max = 200)
    String description;

    @NonNull
    @From(year = 1895, month = DECEMBER, dayOfMonth = 28)
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
    Set<Director> directors;

    @NonFinal
    @NonNull
    MpaRating mpa;

    @NonFinal
    @Setter
    int rate;
}
