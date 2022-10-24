package de.utopiamc.framework.inject.scan;

public class ClassScanFailedException extends RuntimeException {

    public ClassScanFailedException() {
    }

    public ClassScanFailedException(String message) {
        super(message);
    }

    public ClassScanFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClassScanFailedException(Throwable cause) {
        super(cause);
    }

    public ClassScanFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
