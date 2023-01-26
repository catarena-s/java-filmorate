package ru.yandex.practicum.filmorate.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotContainSpaceValidator implements ConstraintValidator<NotContainSpace, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext context) {
        return !s.contains(" ");
    }
}
