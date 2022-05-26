package com.kzumenchuk.testingservice.exception;

public class GeneratingPDFException extends RuntimeException {
    public GeneratingPDFException() {
    }

    public GeneratingPDFException(String message) {
        super(message);
    }

    public GeneratingPDFException(String message, Throwable cause) {
        super(message, cause);
    }

    public GeneratingPDFException(Throwable cause) {
        super(cause);
    }

    public GeneratingPDFException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
