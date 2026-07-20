package ru.albert.consoletodo.errors;

public class WrongException extends Exception {
    public WrongException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public WrongException(Throwable cause) {
        super(cause);
    }

    public WrongException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongException(String message) {
        super(message);
    }

    public WrongException() {
    }
}