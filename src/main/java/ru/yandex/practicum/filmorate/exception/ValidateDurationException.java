package ru.yandex.practicum.filmorate.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(BAD_REQUEST)
public class
ValidateDurationException extends ValidateException{
    public ValidateDurationException(String message) {
        super(message);
    }
}
