package ru.otus.atm;

import java.util.Map;

import ru.otus.banknote.Banknote;
import ru.otus.card.Card;
import ru.otus.operation.ForbiddenOperation;
import ru.otus.operation.Withdrawal;
import ru.otus.storage.Storage;

public class AtmOnlyWithdrawals extends Atm implements Withdrawal {
    public AtmOnlyWithdrawals(Storage storage) {
        super(storage);
    }

    @Override
    public Map<Banknote, Integer> withdrawal(int amount, Card card) throws ForbiddenOperation {
        card.getMoney(amount, card.getCurrency());
        return storage.getMoney(amount, card.getCurrency());
    }
}
