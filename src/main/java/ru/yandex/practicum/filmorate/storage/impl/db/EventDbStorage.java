package ru.yandex.practicum.filmorate.storage.impl.db;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.enums.EventType;
import ru.yandex.practicum.filmorate.model.enums.OperationType;
import ru.yandex.practicum.filmorate.model.impl.Event;
import ru.yandex.practicum.filmorate.storage.EventStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EventDbStorage implements EventStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Event> findByUserId(int userId) {
        String sql = "select * from events where user_id = ?";
        return jdbcTemplate.query(sql, this::mapRowToEvent, userId);
    }

    @Override
    public Event add(@NonNull Event event) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("events")
                .usingGeneratedKeyColumns("event_id");
        Map<String, Object> values = new HashMap<>();
        values.put("event_time", new Timestamp(event.getTimestamp()));
        values.put("event_type", event.getEventType().name());
        values.put("operation", event.getOperation().name());
        values.put("user_id", event.getUserId());
        values.put("entity_id", event.getEntityId());
        event.setId(simpleJdbcInsert.executeAndReturnKey(values).intValue());
        return event;
    }

    @Override
    public Event update(@NonNull Event event) {
        throw new UnsupportedOperationException("Нельзя изменить произошедшее событие");
    }

    @Override
    public List<Event> findAll() {
        throw new UnsupportedOperationException("Сервис не использует этот метод для Event");
    }

    @Override
    public Optional<Event> findById(Long id) {
        throw new UnsupportedOperationException("Сервис не использует этот метод для Event");
    }

    private Event mapRowToEvent(ResultSet rs, int rowNum) throws SQLException {
        return Event.builder()
                .id(rs.getInt("event_id"))
                .timestamp(rs.getTimestamp("event_time").getTime())
                .eventType(EventType.valueOf(rs.getString("event_type")))
                .operation(OperationType.valueOf(rs.getString("operation")))
                .userId(rs.getInt("user_id"))
                .entityId(rs.getInt("entity_id"))
                .build();
    }
}
