package ru.yandex.practicum.filmorate.model.impl;

import lombok.*;
import lombok.experimental.Accessors;
import ru.yandex.practicum.filmorate.model.Entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
//@Builder
//@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"name"})
public class Director implements Entity {

    private long id;

    @NotBlank
    @Size(max = 50)
    private String name;
}
