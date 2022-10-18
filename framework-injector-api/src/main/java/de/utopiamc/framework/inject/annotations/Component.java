package de.utopiamc.framework.inject.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@StaticInjection
@Retention(RUNTIME)
@Target({TYPE, ANNOTATION_TYPE})
public @interface Component {
}
