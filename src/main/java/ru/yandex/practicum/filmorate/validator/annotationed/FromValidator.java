package ru.yandex.practicum.filmorate.validator.annotationed;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.DateTimeException;
import java.time.LocalDate;

@Slf4j
public class FromValidator implements ConstraintValidator<From, LocalDate> {
    private LocalDate from;

    @Override
    public void initialize(From params) {
        try {
            this.from = LocalDate.of(params.year(), params.month(), params.dayOfMonth());
        } catch (DateTimeException e) {
            log.debug("Невозможно преобразовать в дату: "
                    + "year=" + params.year()
                    + ", month=" + params.month()
                    + ", dayOfMonth=" + params.dayOfMonth());
            throw e;
        }
    }

    @Override
    public boolean isValid(LocalDate ld, ConstraintValidatorContext constraintValidatorContext) {
        return !from.isAfter(ld);
    }
}
