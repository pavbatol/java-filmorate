package ru.yandex.practicum.filmorate.validator.annotationed;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FromCinemaDayToCurrentValidator.class)
@Documented
public @interface FromCinemaDayToCurrent {

    String message() default "Дата релиза — не раньше 28 декабря 1895 года и не в будущем";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}