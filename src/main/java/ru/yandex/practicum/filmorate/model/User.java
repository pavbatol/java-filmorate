package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    private int id;

    @NonNull
    @Email
    @EqualsAndHashCode.Include
    private final String email;

    @NonNull
    @Pattern(regexp = "\\S+", message = "Логин не может быть пустым и содержать пробелы")
    private final String login;

    private String name;

    @NonNull
    @Past
    private LocalDate birthday;
}

