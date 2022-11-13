package ru.yandex.practicum.filmorate.service.impl;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.enums.EventType;
import ru.yandex.practicum.filmorate.model.enums.OperationType;
import ru.yandex.practicum.filmorate.model.impl.Event;
import ru.yandex.practicum.filmorate.storage.EventStorage;

import java.time.Instant;
import java.util.List;

import static ru.yandex.practicum.filmorate.model.enums.EventType.*;
import static ru.yandex.practicum.filmorate.model.enums.OperationType.*;

@Service
public class EventService extends AbstractService<Event> {

    private final static String GENERIC_TYPE_NAME = "Событие";
    private final EventStorage eventStorage;

    public EventService(EventStorage eventStorage, EventStorage eventStorage1) {
        super(eventStorage);
        this.eventStorage = eventStorage1;
    }

    @Override
    protected String getGenericTypeName() {
        return GENERIC_TYPE_NAME;
    }

    public List<Event> findByUserId(int userId) {
        return eventStorage.findByUserId(userId);
    }

    public Event addAddedLikeEvent(long userId, long entityId) {
        return eventStorage.add(makeEvent(userId, entityId, LIKE, ADD));
    }

    public Event addRemovedLikeEvent(long userId, long entityId) {
        return eventStorage.add(makeEvent(userId, entityId, LIKE, REMOVE));
    }

    public Event addAddedReviewEvent(long userId, long entityId) {
        return eventStorage.add(makeEvent(userId, entityId, REVIEW, ADD));
    }

    public Event addRemovedReviewEvent(long userId, long entityId) {
        return eventStorage.add(makeEvent(userId, entityId, REVIEW, REMOVE));
    }

    public Event addUpdatedReviewEvent(long userId, long entityId) {
        return eventStorage.add(makeEvent(userId, entityId, REVIEW, UPDATE));
    }

    public Event addAddedFriendEvent(long userId, long entityId) {
        return eventStorage.add(makeEvent(userId, entityId, FRIEND, ADD));
    }

    public Event addRemovedFriendEvent(long userId, long entityId) {
        return eventStorage.add(makeEvent(userId, entityId, FRIEND, REMOVE));
    }

    private Event makeEvent(long userId, long entityId, EventType eT, OperationType oT) {
        return Event.builder()
                .id(0)
                .timestamp(Instant.now().toEpochMilli())
                .eventType(eT)
                .operation(oT)
                .userId(userId)
                .entityId(entityId)
                .build();
    }
}
