package ru.otus.atm;

import ru.otus.storage.Storage;

public abstract class Atm {
    protected final Storage storage;

    protected Atm(Storage storage) {
        this.storage = storage;
    }

    public int getBalance() {
        return storage.getBalance();
    }
}
