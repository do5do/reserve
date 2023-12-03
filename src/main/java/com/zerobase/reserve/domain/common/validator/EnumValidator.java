package com.zerobase.reserve.domain.common.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Enum 타입의 파라미터를 검증하는 validator입니다.
 */
public class EnumValidator implements ConstraintValidator<ValidEnum, Enum<?>> {
    private ValidEnum annotation;

    @Override
    public void initialize(ValidEnum constraintAnnotation) {
        annotation = constraintAnnotation;
    }

    /**
     * Enum 값을 검증하는 메소드입니다.
     * 값이 없거나 해당 Enum 클래스에 정의된 값이 아니라면 false를 반환합니다.
     *
     * @param value object to validate
     * @param context context in which the constraint is evaluated
     *
     * @return 검증 확인 명제
     */
    @Override
    public boolean isValid(Enum value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        Enum<?>[] enumValues = annotation.enumClass().getEnumConstants();
        if (enumValues != null) {
            for (Enum<?> enumValue : enumValues) {
                if (value == enumValue) {
                    return true;
                }
            }
        }

        return false;
    }
}
