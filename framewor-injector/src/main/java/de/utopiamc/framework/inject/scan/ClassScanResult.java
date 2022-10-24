package de.utopiamc.framework.inject.scan;

import lombok.Data;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

@Data
public final class ClassScanResult<T> {

    private final Class<T> type;
    private final Constructor<?> constructor;
    private final Set<Field> injectFields;
    private final Set<Method> injectMethods;

}
