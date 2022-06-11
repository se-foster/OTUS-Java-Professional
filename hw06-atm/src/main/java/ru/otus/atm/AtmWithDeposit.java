package ru.otus.atm;

import java.util.Map;

import ru.otus.banknote.Banknote;
import ru.otus.operation.Deposit;
import ru.otus.storage.Storage;

public class AtmWithDeposit extends AtmOnlyWithdrawals implements Deposit {
    public AtmWithDeposit(Storage storage) {
        super(storage);
    }

    @Override
    public void deposit(Map<Banknote, Integer> banknotesToDeposit) {
        banknotesToDeposit.forEach(storage::addBanknotes);
    }
}
