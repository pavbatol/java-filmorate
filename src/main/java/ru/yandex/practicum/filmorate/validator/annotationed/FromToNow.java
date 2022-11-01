package ru.yandex.practicum.filmorate.validator.annotationed;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.time.Month;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FromToNowValidator.class)
@Documented
public @interface FromToNow {
    String message() default "Дата вне допустимого диапазона";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int year();

    Month month();

    int dayOfMonth();
}
