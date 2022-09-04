package ru.yandex.practicum.filmorate.validator.annotationed;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.temporal.Temporal;

public class PastCinemaBirthdayValidator implements ConstraintValidator<PastCinemaBirthday, Temporal> {

    @Override
    public boolean isValid(Temporal temporal, ConstraintValidatorContext constraintValidatorContext) {
        if (temporal == null) {
            return false;
        }
        LocalDate ld = LocalDate.from(temporal);
        return !ld.isBefore(LocalDate.of(1895, 12, 28));
    }
}
