package com.go2wheel.weblizedutil.validator;

import java.util.Arrays;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.go2wheel.weblizedutil.annotation.ShowPossibleValue;

public class PossibleValueValidator implements ConstraintValidator<ShowPossibleValue, String> {
	
	private ShowPossibleValue constraintAnnotation;
	
	
	@Override
	public void initialize(ShowPossibleValue constraintAnnotation) {
		ConstraintValidator.super.initialize(constraintAnnotation);
		this.constraintAnnotation = constraintAnnotation;
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		String[] values = constraintAnnotation.value();
		return Arrays.stream(values).anyMatch(v -> v.equals(value));
	}

}
