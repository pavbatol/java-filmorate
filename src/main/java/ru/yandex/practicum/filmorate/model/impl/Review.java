package ru.yandex.practicum.filmorate.model.impl;

import lombok.*;
import ru.yandex.practicum.filmorate.model.Entity;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Review implements Entity {

    @EqualsAndHashCode.Include
    private long id;

    @NonNull
    private String content;

    @NonNull
    private Boolean isPositive;

    private int useful;

    @NonNull
    private Long userId;

    @NonNull
    private Long filmId;
}
