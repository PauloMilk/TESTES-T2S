package com.t2s.conteiner.helper.validator;

import com.t2s.conteiner.helper.annotation.TipoConteinerPattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TipoConteinerPatternValidator implements ConstraintValidator<TipoConteinerPattern, Integer> {


    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        if (value == 20 || value == 40) {
            return true;
        }
        return false;
    }
}
