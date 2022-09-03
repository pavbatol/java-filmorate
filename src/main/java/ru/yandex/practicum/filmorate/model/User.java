package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    private int id;

    @NonNull
    @EqualsAndHashCode.Include
    private final String email;

    @NonNull
    private final String login;

    private String name;

    @NonNull
    private LocalDate birthday;
}

