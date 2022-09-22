package ru.yandex.practicum.filmorate.model.impl;

import lombok.*;
import lombok.experimental.NonFinal;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Email;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.Set;

@Value
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class User extends AbstractEntity {

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

    @NonFinal
    @Setter
    Set<Long> friends;
}

