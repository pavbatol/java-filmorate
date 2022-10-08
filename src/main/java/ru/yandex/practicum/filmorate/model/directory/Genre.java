package ru.yandex.practicum.filmorate.model.directory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(exclude = {"name"})
public class Genre {

    private final int id;

    @Size(max = 30)
    private String name;
}
