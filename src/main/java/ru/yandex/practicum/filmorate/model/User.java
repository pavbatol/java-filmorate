package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
public class User {

    private long id;

    @NonNull
    @Email
    private final String email;

    @NonNull
    @Pattern(regexp = "\\S+")
    private final String login;

    private String name;

    @NonNull
    @Past
    private LocalDate birthday;
}

