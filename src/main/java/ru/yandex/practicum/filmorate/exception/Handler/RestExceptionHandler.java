package ru.yandex.practicum.filmorate.exception.Handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidateException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<Object> handleNotFoundEx(RuntimeException ex, WebRequest request) {
        String message = "Объект не найден";
        return getResponseEntity(message, ex, NOT_FOUND, request);
    }

    @ExceptionHandler({ValidateException.class, ConstraintViolationException.class})
    protected ResponseEntity<Object> handleValidateEx(RuntimeException ex, WebRequest request) {
        String message = "Некорректные данные";
        return getResponseEntity(message, ex, BAD_REQUEST, request);
    }

    @NonNull
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NonNull MethodArgumentNotValidException ex,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatus status,
                                                                  @NonNull WebRequest request) {
        String message = "Некорректные данные";
        return getResponseEntity(message, ex, status, request);
    }

    @NonNull
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(@NonNull HttpMessageNotReadableException ex,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatus status,
                                                                  @NonNull WebRequest request) {
        String message = "Нечитаемый JSON";
        return getResponseEntity(message, ex, status, request);
    }

    @NonNull
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(@NonNull NoHandlerFoundException ex,
                                                                   @NonNull HttpHeaders headers,
                                                                   @NonNull HttpStatus status,
                                                                   @NonNull WebRequest request) {
        String message = "Обработчик для энд-поинта не найден";
        return getResponseEntity(message, ex, status, request);
    }

    private ResponseEntity<Object> getResponseEntity(String message,
                                                     Exception ex,
                                                     HttpStatus status,
                                                     WebRequest request) {

        log.error(message + ": {}", ex.getMessage(), ex);
        Body body =  getNewBody(message, status, request, ex);
        return new ResponseEntity<>(body, status);
    }

    private Body getNewBody(String message, HttpStatus status, WebRequest request, Exception ex) {
        List<String> reasons;
        if (ex instanceof BindException) {
            reasons = ((BindException) ex)
                    .getAllErrors()
                    .stream()
                    .map(this::getErrorString)
                    .collect(Collectors.toList());
        } else {
            reasons = Arrays.stream(ex.getMessage().split(", ")).collect(Collectors.toList());
        }
        return Body.builder()
                .status(status.value())
                .endPoint(getRequestURI(request))
                .detailMessage(message)
                .error(status.getReasonPhrase())
                .reasons(reasons)
                .build();
    }

    private String getErrorString(ObjectError error) {
        if (error instanceof FieldError) {
            return ((FieldError) error).getField() + ' ' + error.getDefaultMessage();
        }
        return error.getDefaultMessage();
    }

    private String getRequestURI(WebRequest request) {
        if (request instanceof ServletWebRequest) {
            HttpServletRequest requestHttp = ((ServletWebRequest) request).getRequest();
            return String.format("%s %s", requestHttp.getMethod(), requestHttp.getRequestURI());
        } else {
            return "";
        }
    }
}

@Value
@Builder
class Body {
    OffsetDateTime timestamp = OffsetDateTime.now();
    int status;
    String endPoint;
    @JsonInclude(NON_NULL)
    String detailMessage;
    String error;
    List<String> reasons;
}




