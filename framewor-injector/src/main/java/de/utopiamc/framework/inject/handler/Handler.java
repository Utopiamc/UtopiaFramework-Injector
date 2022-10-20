package de.utopiamc.framework.inject.handler;

import java.lang.annotation.Annotation;

public interface Handler {

    HandlerResult handleAnnotation(Annotation annotation, Class<?> onClass);

}
