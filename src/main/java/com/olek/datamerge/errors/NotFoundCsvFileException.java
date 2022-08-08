package com.olek.datamerge.errors;

public class NotFoundCsvFileException extends RuntimeException {
    public NotFoundCsvFileException(String message) {
        super(message);
    }
}
