package com.olek.datamerge.errors;

public class EmptyOutputPathException extends RuntimeException {
    public EmptyOutputPathException(String message) {
        super(message);
    }
}
