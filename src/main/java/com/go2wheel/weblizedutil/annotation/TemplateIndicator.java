package com.go2wheel.weblizedutil.annotation;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Instruction for value provider.
 * 
 * @author jianglibo@gmail.com
 *
 */
@Target(PARAMETER)
@Retention(RUNTIME)
public @interface TemplateIndicator {

}
