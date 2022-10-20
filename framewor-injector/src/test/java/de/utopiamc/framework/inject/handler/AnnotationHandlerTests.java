package de.utopiamc.framework.inject.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith({MockitoExtension.class})
public class AnnotationHandlerTests {

    private AnnotationHandler underTest;

    @Retention(RetentionPolicy.RUNTIME)
    @interface TestAnnotation{}

    @Retention(RetentionPolicy.RUNTIME)
    @interface TestAnnotation2{}

    @TestAnnotation
    class TestClass {}

    @BeforeEach
    void setUp() {
        this.underTest = new AnnotationHandler();
    }

    @Test
    void itShouldHookHandler() {
        // given
        Class<TestAnnotation> testAnnotation = TestAnnotation.class;
        Handler handler = (annotation, cls) -> null;

        // when
        underTest.hookAnnotationHandler(testAnnotation, handler);

        // then
        assertThat(underTest.getAnnotationHandlers(testAnnotation))
                .containsExactly(handler);
    }

    @Test
    void itShouldHookAllHandlers() {
        // given
        Class<TestAnnotation> testAnnotation = TestAnnotation.class;
        Handler handler = (annotation, cls) -> null;
        Handler handler2 = (annotation, cls) -> null;

        // when
        underTest.hookAnnotationHandler(testAnnotation, handler);
        underTest.hookAnnotationHandler(testAnnotation, handler2);

        // then
        assertThat(underTest.getAnnotationHandlers(testAnnotation))
                .containsOnly(handler, handler2);
    }

    @Test
    void itShouldHookOnlySpecificHandlers() {
        // given
        Class<TestAnnotation> testAnnotation = TestAnnotation.class;
        Class<TestAnnotation2> testAnnotation2 = TestAnnotation2.class;
        Handler handler = (annotation, cls) -> null;
        Handler handler2 = (annotation, cls) -> null;

        // when
        underTest.hookAnnotationHandler(testAnnotation, handler);
        underTest.hookAnnotationHandler(testAnnotation2, handler2);

        // then
        assertThat(underTest.getAnnotationHandlers(testAnnotation))
                .containsOnly(handler);
    }

    @Test
    void itShouldInvokeHandler(@Mock Handler testAnnotationHandler) {
        // given
        Class<TestClass> testClass = TestClass.class;

        underTest.hookAnnotationHandler(TestAnnotation.class, testAnnotationHandler);

        // when
        underTest.handleAnnotations(testClass);

        // then
        verify(testAnnotationHandler).handleAnnotation(testClass.getAnnotation(TestAnnotation.class), testClass);
    }
}
