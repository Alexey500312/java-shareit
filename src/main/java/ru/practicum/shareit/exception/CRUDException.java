package ru.practicum.shareit.exception;

public class CRUDException extends RuntimeException {
    public CRUDException(String message) {
        super(message);
    }
}
