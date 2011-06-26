package com.wasome.space_command.behavior;
import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Visible {
	public VISIBIBILITY when() default VISIBIBILITY.ALWAYS;
	
	public enum VISIBIBILITY {
		ALWAYS,
		CONDITIONALLY
	}
}
