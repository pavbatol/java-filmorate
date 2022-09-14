package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.NonFinal;

import javax.validation.constraints.Email;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Value
@Builder(toBuilder = true)
@AllArgsConstructor
public class User {

    @NonFinal
    @Setter
    long id;

    @NonNull
    @Email
    String email;

    @NonNull
    @Pattern(regexp = "\\S+")
    String login;

    @NonFinal
    @Setter
    String name;

    @NonNull
    @Past
    LocalDate birthday;
}

