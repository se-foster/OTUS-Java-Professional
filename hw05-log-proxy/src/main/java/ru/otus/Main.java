package ru.otus;

public class Main {
    public static void main(String[] args) {
        Logging logging = LoggingProxy.getProxy(new TestLogging());

        logging.calculation(1);
        logging.calculation(3, 4);
        logging.calculation(5, 7, "9");
    }
}
