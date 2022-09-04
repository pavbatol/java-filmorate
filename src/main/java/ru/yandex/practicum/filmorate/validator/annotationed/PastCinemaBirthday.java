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
@Constraint(validatedBy = PastCinemaBirthdayValidator.class)
@Documented
public @interface PastCinemaBirthday {

    String message() default "it.example.validator.MyPast.message";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}