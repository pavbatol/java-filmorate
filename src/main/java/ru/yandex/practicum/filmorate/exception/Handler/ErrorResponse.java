package ru.yandex.practicum.filmorate.exception.Handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;

import java.time.OffsetDateTime;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Value
@Builder
public class ErrorResponse {
    OffsetDateTime timestamp = OffsetDateTime.now();
    int status;
    String endPoint;
    @JsonInclude(NON_NULL)
    String detailMessage;
    String error;
    List<String> reasons;
}
