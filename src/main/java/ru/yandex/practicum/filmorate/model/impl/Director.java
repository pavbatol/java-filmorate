package ru.yandex.practicum.filmorate.model.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.model.Entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"name"})
public class Director implements Entity {

    private long id;

    @NotBlank
    @Size(max = 50)
    private String name;
}
