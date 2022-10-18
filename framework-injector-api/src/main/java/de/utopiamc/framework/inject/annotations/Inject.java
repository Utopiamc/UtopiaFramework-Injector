package de.utopiamc.framework.inject.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({CONSTRUCTOR, METHOD, FIELD})
public @interface Inject {
}
