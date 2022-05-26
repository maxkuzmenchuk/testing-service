package com.kzumenchuk.testingservice.exception;

public class TestNotFoundException extends RuntimeException {
    public TestNotFoundException() {
    }

    public TestNotFoundException(String message) {
        super(message);
    }

    public TestNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public TestNotFoundException(Throwable cause) {
        super(cause);
    }

    public TestNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
