package ru.yandex.practicum.filmorate.model.impl;

import lombok.*;
import lombok.experimental.SuperBuilder;
import ru.yandex.practicum.filmorate.model.Entity;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public abstract class AbstractEntity implements Entity {
    private long id = 0;
}
