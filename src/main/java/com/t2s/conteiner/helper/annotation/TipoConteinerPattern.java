package com.t2s.conteiner.helper.annotation;

import com.t2s.conteiner.helper.validator.TipoConteinerPatternValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = TipoConteinerPatternValidator.class)
public @interface TipoConteinerPattern {
    String message() default "must match teste";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
