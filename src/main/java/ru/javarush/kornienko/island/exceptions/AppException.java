package ru.javarush.kornienko.island.exceptions;

public class AppException extends RuntimeException {
    public AppException(String message, Throwable cause) {
        super(message, cause);
    }
}