package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.time.LocalDate;

@Data
public class Film {
    //@Setter(AccessLevel.PROTECTED)
    //@EqualsAndHashCode.Exclude
    private int id;

    @NonNull
    private final String name;

    @EqualsAndHashCode.Exclude
    private String description;

    @NonNull
    private final LocalDate releaseDate;

    private final long duration;
}
