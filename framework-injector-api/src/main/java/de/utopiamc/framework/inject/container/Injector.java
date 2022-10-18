package de.utopiamc.framework.inject.container;

import java.lang.reflect.Type;

public interface Injector {

    <T> T getInstance(Class<T> type);

    <T> T getInstance(Type type);

}
