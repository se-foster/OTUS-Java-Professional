package ru.otus.atm;

import ru.otus.card.Card;
import ru.otus.operation.Balance;
import ru.otus.storage.Storage;

public abstract class Atm implements Balance {
    protected final Storage storage;

    protected Atm(Storage storage) {
        this.storage = storage;
    }

    @Override
    public String getBalance(Card card) {
        return "" + card.getBalance() + card.getCurrency().name();
    }
}
