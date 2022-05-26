package com.kzumenchuk.testingservice.exception;

public class FileDataNotFoundException extends RuntimeException {
    public FileDataNotFoundException() {
    }

    public FileDataNotFoundException(String message) {
        super(message);
    }

    public FileDataNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileDataNotFoundException(Throwable cause) {
        super(cause);
    }

    public FileDataNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
