package com.kzumenchuk.testingservice.exception;

public class OptionNotFoundException extends RuntimeException {
    public OptionNotFoundException() {
    }

    public OptionNotFoundException(String message) {
        super(message);
    }

    public OptionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public OptionNotFoundException(Throwable cause) {
        super(cause);
    }

    public OptionNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
