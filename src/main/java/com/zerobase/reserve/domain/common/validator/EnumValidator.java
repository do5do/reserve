package com.zerobase.reserve.domain.common.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<ValidEnum, Enum<?>> {
    private ValidEnum annotation;

    @Override
    public void initialize(ValidEnum constraintAnnotation) {
        annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(Enum value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        } else {
            Enum<?>[] enumValues = annotation.enumClass().getEnumConstants();
            if (enumValues != null) {
                for (Enum<?> enumValue : enumValues) {
                    if (value == enumValue) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
