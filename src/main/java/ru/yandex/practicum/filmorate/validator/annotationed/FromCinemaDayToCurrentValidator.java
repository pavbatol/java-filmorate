package ru.yandex.practicum.filmorate.validator.annotationed;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.temporal.Temporal;

import static java.time.Month.DECEMBER;

public class FromCinemaDayToCurrentValidator implements ConstraintValidator<FromCinemaDayToCurrent, Temporal> {

    @Override
    public boolean isValid(Temporal temporal, ConstraintValidatorContext constraintValidatorContext) {
        if (temporal == null) {
            return false;
        }
        LocalDate ld = LocalDate.from(temporal);
        return !ld.isBefore(LocalDate.of(1895, DECEMBER, 28))
                && !ld.isAfter(LocalDate.now());
    }
}
