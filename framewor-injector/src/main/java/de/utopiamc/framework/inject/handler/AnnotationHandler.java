package de.utopiamc.framework.inject.handler;

import de.utopiamc.commons.utils.AnnotationUtil;
import de.utopiamc.commons.validate.Validator;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AnnotationHandler {

    private final Map<Class<? extends Annotation>, Set<Handler>> annotationHandlers;

    public AnnotationHandler() {
        this.annotationHandlers = new HashMap<>();
    }

    public <T extends Annotation> void hookAnnotationHandler(Class<T> annotation, Handler handler) {
        annotationHandlers.computeIfAbsent(annotation, (key) -> new HashSet<>())
                .add(handler);
    }

    public <T extends Annotation> Set<Handler> getAnnotationHandlers(Class<T> annotationType) {
        return annotationHandlers.get(annotationType);
    }

    public Set<HandlerResult> handleAnnotations(Class<?> annotatedClass) {
        Validator.requireNonNull(annotatedClass, String.format("'%s' should not be null!", annotatedClass));

        Set<HandlerResult> handlerResults = new HashSet<>();
        for (Annotation annotation : AnnotationUtil.getAnnotations(annotatedClass)) {
            Set<Handler> handlers = annotationHandlers.get(annotation.annotationType());
            for (Handler handler : handlers) {
                handlerResults.add(handler.handleAnnotation(annotation, annotatedClass));
            }
        }

        return handlerResults;
    }
}
