package de.utopiamc.framework.inject.handler;

import java.lang.annotation.Annotation;
import java.util.Set;

public interface IAnnotationHandler {

    <T extends Annotation> void hookAnnotationHandler(Class<T> annotation, Handler handler);
    <T extends Annotation> Set<Handler> getAnnotationHandlers(Class<T> annotationType);
    Set<HandlerResult> handleAnnotations(Class<?> annotatedClass);

}
