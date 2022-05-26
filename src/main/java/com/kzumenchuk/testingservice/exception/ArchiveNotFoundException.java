package com.kzumenchuk.testingservice.exception;

public class ArchiveNotFoundException extends RuntimeException {
    public ArchiveNotFoundException() {
    }

    public ArchiveNotFoundException(String message) {
        super(message);
    }

    public ArchiveNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ArchiveNotFoundException(Throwable cause) {
        super(cause);
    }

    public ArchiveNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
