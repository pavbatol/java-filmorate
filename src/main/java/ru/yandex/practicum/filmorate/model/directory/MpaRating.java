package ru.yandex.practicum.filmorate.model.directory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(exclude = {"rating", "description"})
public class MpaRating {

    private final int id;

    @Size(max = 10)
    private String rating;

    @Size(max = 150)
    private String description;
}
