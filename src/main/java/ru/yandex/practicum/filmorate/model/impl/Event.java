package ru.yandex.practicum.filmorate.model.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.yandex.practicum.filmorate.model.Entity;
import ru.yandex.practicum.filmorate.model.enums.EventType;
import ru.yandex.practicum.filmorate.model.enums.OperationType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Event implements Entity {

    @EqualsAndHashCode.Include
    @JsonProperty(value="eventId")
    private long id;

    @NonNull
    private Long timestamp;

    private EventType eventType;

    private OperationType operation;

    private long userId;

    private long entityId;
}
