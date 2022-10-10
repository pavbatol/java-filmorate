package ru.yandex.practicum.filmorate.exception.EntityValidation;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(BAD_REQUEST)
public class ValidateException extends RuntimeException {
    public ValidateException(String message) {
        super(message);
    }
}