package de.utopiamc.framework.inject.scan;

import de.utopiamc.commons.utils.AnnotationUtil;
import de.utopiamc.commons.validate.Validator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class ClassScanner implements IClassScanner {

    private static final Set<Class<? extends Annotation>> INJECTABLES = Set.of(javax.inject.Inject.class, de.utopiamc.framework.inject.annotations.Inject.class);

    @Override
    public <T> ClassScanResult<T> scanClass(Class<T> cls) {
        Validator.requireNonNull(cls, "Class should not be null!");

        return new ClassScanResult<>(cls,
                getSuitableConstructor(cls),
                getInjectableFields(cls),
                null);
    }

    private Set<Field> getInjectableFields(Class<?> cls) {
        Set<Field> fields = new HashSet<>();

        for (Field declaredField : cls.getDeclaredFields()) {
            for (Class<? extends Annotation> injectable : INJECTABLES) {
                if (AnnotationUtil.isAnnotationPresent(declaredField, injectable)) {
                    declaredField.setAccessible(true);
                    fields.add(declaredField);
                }
            }
        }

        if (cls.getSuperclass()!=null)
            fields.addAll(getInjectableFields(cls.getSuperclass()));

        return fields;
    }

    private Constructor<?> getSuitableConstructor(Class<?> cls) {
        Constructor<?> suitableConstructor = null;
        constructor:
        for (Constructor<?> constructor : cls.getConstructors()) {
            for (Class<? extends Annotation> injectable : INJECTABLES) {
                if (AnnotationUtil.isAnnotationPresent(constructor, injectable)) {
                    if (suitableConstructor == null)
                        suitableConstructor = constructor;
                    else
                        throw new ConstructorScanFailedException(String.format("There are more than one injectable annotated constructors in '%s'", cls.getName()));
                    continue constructor;
                }
            }
        }

        if (suitableConstructor == null) {
            if (cls.getConstructors().length == 1)
                suitableConstructor = cls.getConstructors()[0];
            else if (cls.getConstructors().length > 1)
                throw new ConstructorScanFailedException(String.format("There are to many not '%s' annotated constructors in '%s'!", javax.inject.Inject.class.getName(), cls.getName()));
            else
                throw new ConstructorScanFailedException(String.format("There is no public constructor in '%s'!", cls.getName()));
        }

        return suitableConstructor;
    }

}
