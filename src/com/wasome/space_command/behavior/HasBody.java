package com.wasome.space_command.behavior;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
/**
 * Entity has physics body.
 * 
 * Dictates certain behaviors, like whether to try to delete the body when the entity is destroyed.
 */
public @interface HasBody {

}
