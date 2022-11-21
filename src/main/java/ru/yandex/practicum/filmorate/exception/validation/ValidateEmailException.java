package ru.yandex.practicum.filmorate.exception.validation;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(BAD_REQUEST)
public class ValidateEmailException extends ValidateException {
    public ValidateEmailException(String message) {
        super(message);
    }
}
