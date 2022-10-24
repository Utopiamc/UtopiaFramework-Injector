package de.utopiamc.framework.inject.scan;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ClassScannerTests {

    private ClassScanner underTest;

    @BeforeEach
    void setUp() {
        this.underTest = new ClassScanner();
    }

    @Test
    void itShouldFindInjectableFields() throws NoSuchFieldException {
        // given
        Class<ClassWithNoArgsConstructorAndInjectableField> classWithInjectableField = ClassWithNoArgsConstructorAndInjectableField.class;

        //when
        ClassScanResult<ClassWithNoArgsConstructorAndInjectableField> result = underTest.scanClass(classWithInjectableField);

        //then
        assertThat(result.getInjectFields())
                .containsOnly(classWithInjectableField.getDeclaredField("inject"))
                .allMatch(Field::isAccessible);
    }

    @Test
    void itShouldFindAllInjectableFields() throws NoSuchFieldException {
        // given
        Class<ClassWithAnnotatedConstructorAndManyInjectableFields> classWithManyInjectableFields = ClassWithAnnotatedConstructorAndManyInjectableFields.class;

        //when
        ClassScanResult<ClassWithAnnotatedConstructorAndManyInjectableFields> result = underTest.scanClass(classWithManyInjectableFields);

        //then
        assertThat(result.getInjectFields())
                .containsOnly(classWithManyInjectableFields.getDeclaredField("test"), classWithManyInjectableFields.getDeclaredField("test1"))
                .allMatch(Field::isAccessible);
    }

    @Test
    void itShouldFindOnlyInjectableFields() throws NoSuchFieldException {
        // given
        Class<ClassWithNonAnnotatedConstructorAndFieldsWithOneInjectable> classWithFieldsAndOneInjectable = ClassWithNonAnnotatedConstructorAndFieldsWithOneInjectable.class;

        //when
        ClassScanResult<ClassWithNonAnnotatedConstructorAndFieldsWithOneInjectable> result = underTest.scanClass(classWithFieldsAndOneInjectable);

        //then
        assertThat(result.getInjectFields())
                .containsOnly(classWithFieldsAndOneInjectable.getDeclaredField("test"))
                .allMatch(Field::isAccessible);
    }

    @Test
    @DisplayName("It should find NoArgsConstructor")
    void itShouldFindNoArgsConstructor() throws NoSuchMethodException {
        // given
        Class<ClassWithNoArgsConstructorAndInjectableField> classWithNoArgsConstructor = ClassWithNoArgsConstructorAndInjectableField.class;

        // when
        ClassScanResult<ClassWithNoArgsConstructorAndInjectableField> result = underTest.scanClass(classWithNoArgsConstructor);

        // then
        assertThat(result.getConstructor())
                .isNotNull()
                .isEqualTo(classWithNoArgsConstructor.getConstructor());
    }

    @Test
    @DisplayName("It should find not annotated Constructor")
    void itShouldFindNonAnnotatedConstructor() throws NoSuchMethodException {
        // given
        Class<ClassWithNonAnnotatedConstructorAndFieldsWithOneInjectable> classWithNonAnnotatedConstructorClass = ClassWithNonAnnotatedConstructorAndFieldsWithOneInjectable.class;

        // when
        ClassScanResult<ClassWithNonAnnotatedConstructorAndFieldsWithOneInjectable> result = underTest.scanClass(classWithNonAnnotatedConstructorClass);

        // then
        assertThat(result.getConstructor())
                .isNotNull()
                .isEqualTo(classWithNonAnnotatedConstructorClass.getConstructor(String.class));
    }

    @Test
    @DisplayName("It should find annotated Constructor")
    void itShouldFindAnnotatedConstructor() throws NoSuchMethodException {
        // given
        Class<ClassWithAnnotatedConstructorAndManyInjectableFields> classWithAnnotatedConstructorClass = ClassWithAnnotatedConstructorAndManyInjectableFields.class;

        // when
        ClassScanResult<ClassWithAnnotatedConstructorAndManyInjectableFields> result = underTest.scanClass(classWithAnnotatedConstructorClass);

        // then
        assertThat(result.getConstructor())
                .isNotNull()
                .isEqualTo(classWithAnnotatedConstructorClass.getConstructor(String.class));
    }

    @Test
    @DisplayName("It should throw ConstructorScanFailedException with multiple not annotated Constructors")
    void itShouldThrowWithMultipleNonAnnotatedConstructors() {
        // given
        Class<ClassWithMultipleNonAnnotatedConstructors> classWithMultipleNonAnnotatedConstructorsClass = ClassWithMultipleNonAnnotatedConstructors.class;

        // when
        // then
        assertThatThrownBy(() -> underTest.scanClass(classWithMultipleNonAnnotatedConstructorsClass))
                .isExactlyInstanceOf(ConstructorScanFailedException.class)
                .hasMessageContaining("There are to many");
    }

    @Test
    @DisplayName("It should throw ConstructorScanFailedException with multiple annotated Constructors")
    void itShouldThrowWithMultipleAnnotatedConstructor() {
        // given
        Class<ClassWithMultipleAnnotatedConstructors> classWithMultipleAnnotatedConstructorsClass = ClassWithMultipleAnnotatedConstructors.class;

        // when
        // then
        assertThatThrownBy(() -> underTest.scanClass(classWithMultipleAnnotatedConstructorsClass))
                .isExactlyInstanceOf(ConstructorScanFailedException.class)
                .hasMessageContaining("There are more than one injectable annotated constructors in");
    }

    @Test
    @DisplayName("It should throw ConstructorScanFailedException with multiple no public Constructors")
    void itShouldThrowWithNoPublicConstructor() {
        // given
        Class<ClassWithNoPublicConstructor> classWithNoPublicConstructorClass = ClassWithNoPublicConstructor.class;

        // when
        // then
        assertThatThrownBy(() -> underTest.scanClass(classWithNoPublicConstructorClass))
                .isExactlyInstanceOf(ConstructorScanFailedException.class)
                .hasMessageContaining("There is no public constructor in");
    }

    static class ClassWithNoArgsConstructorAndInjectableField {
        @Inject
        private String inject;

        public ClassWithNoArgsConstructorAndInjectableField() {}
    }

    static class ClassWithNonAnnotatedConstructorAndFieldsWithOneInjectable {

        @Inject private String test;
        private String test1;

        public ClassWithNonAnnotatedConstructorAndFieldsWithOneInjectable(String string) {}
    }

    static class ClassWithAnnotatedConstructorAndManyInjectableFields {

        @Inject private String test;
        @Inject private String test1;

        @Inject
        public ClassWithAnnotatedConstructorAndManyInjectableFields(String string) {}

        public ClassWithAnnotatedConstructorAndManyInjectableFields() {}

    }

    static class ClassWithMultipleNonAnnotatedConstructors {
        public ClassWithMultipleNonAnnotatedConstructors(String test){}
        public ClassWithMultipleNonAnnotatedConstructors(){}
    }

    static class ClassWithMultipleAnnotatedConstructors {
        @Inject
        public ClassWithMultipleAnnotatedConstructors(String test){}

        @Inject
        public ClassWithMultipleAnnotatedConstructors(){}
    }

    static class ClassWithNoPublicConstructor {
        private ClassWithNoPublicConstructor() {}
    }

}
