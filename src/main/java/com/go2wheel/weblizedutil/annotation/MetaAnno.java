package com.go2wheel.weblizedutil.annotation;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Target;

@Target({ TYPE, PARAMETER })
public @interface MetaAnno {
	
	String value() default "";

}
