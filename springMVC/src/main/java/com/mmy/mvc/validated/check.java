package com.mmy.mvc.validated;

import com.mmy.mvc.enums.CheckType;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.*;

/**
 * @author : kebukeyi
 * @date :  2021-09-18 21:07
 * @description :
 * @question :
 * @usinglink :
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Constraint(validatedBy = check.CheckValidator.class)
public @interface check {

    String message() default ""; // 自定义异常返回信息

    CheckType type(); // 自定义校验字段

    Class<?>[] groups() default {};

    Class<? extends javax.validation.Payload>[] payload() default {};


    class CheckValidator implements ConstraintValidator<check, Object> {

        @Override
        public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
            return false;
        }
    }
}
