package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.NonFinal;
import ru.yandex.practicum.filmorate.validator.annotationed.FromToNow;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

import static java.time.Month.DECEMBER;

@Value
@Builder(toBuilder = true)
@AllArgsConstructor
public class Film {

    @NonFinal
    @Setter
    long id;

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
}
