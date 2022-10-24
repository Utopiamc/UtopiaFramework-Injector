package de.utopiamc.framework.inject.scan;

public interface IClassScanner {

    <T> ClassScanResult<T> scanClass(Class<T> cls);

}
