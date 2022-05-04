package com.kzumenchuk.testingservice.exception;

public class TestAlreadyExistsException extends RuntimeException {
    public TestAlreadyExistsException() {
    }

    public TestAlreadyExistsException(String message) {
        super(message);
    }

    public TestAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public TestAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    public TestAlreadyExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
