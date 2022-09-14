package ru.yandex.practicum.filmorate.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(BAD_REQUEST)
public class ValidateNameException extends ValidateException {
    public ValidateNameException(String message) {
        super(message);
    }
}
