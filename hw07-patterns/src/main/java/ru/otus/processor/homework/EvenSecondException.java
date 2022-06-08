package ru.otus.processor.homework;

public class EvenSecondException extends RuntimeException {
    public EvenSecondException() {
        super("каждую четную секунду происходит что-то плохое");
    }
}
