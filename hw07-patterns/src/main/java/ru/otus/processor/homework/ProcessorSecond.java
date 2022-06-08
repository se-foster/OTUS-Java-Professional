package ru.otus.processor.homework;

import java.sql.Date;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

public class ProcessorSecond implements Processor {

    private final DateTimeProvider dateTimeProvider;

    public ProcessorSecond(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public Message process(Message message) {
        long seconds = dateTimeProvider.getSeconds();
        if (seconds % 2 == 0) {
            throw new EvenSecondException();
        }
        return message;
    }
}
