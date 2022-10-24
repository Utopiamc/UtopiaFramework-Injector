package de.utopiamc.framework.inject.scan;

public class ConstructorScanFailedException extends ClassScanFailedException {

    public ConstructorScanFailedException() {
    }

    public ConstructorScanFailedException(String message) {
        super(message);
    }

    public ConstructorScanFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConstructorScanFailedException(Throwable cause) {
        super(cause);
    }

    public ConstructorScanFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
