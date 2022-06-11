package ru.otus.operation;

public class ForbiddenOperation extends Exception {
    public ForbiddenOperation(String message) {
        super(message);
    }
}
