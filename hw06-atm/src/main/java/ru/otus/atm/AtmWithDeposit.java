package ru.otus.atm;

import java.util.Map;

import ru.otus.banknote.Banknote;
import ru.otus.card.Card;
import ru.otus.operation.Deposit;
import ru.otus.operation.ForbiddenOperation;
import ru.otus.storage.Storage;

public class AtmWithDeposit extends AtmOnlyWithdrawals implements Deposit {
    public AtmWithDeposit(Storage storage) {
        super(storage);
    }

    @Override
    public int deposit(Map<Banknote, Integer> banknotesToDeposit, Card card) throws ForbiddenOperation {
        checkMoney(banknotesToDeposit, card);
        int result = banknotesToDeposit.keySet().stream()
                .mapToInt(banknote -> storage.addBanknotes(banknote, banknotesToDeposit.get(banknote)))
                .sum();
        card.insertMoney(result, card.getCurrency());
        return result;
    }

    private void checkMoney(Map<Banknote, Integer> banknotesToDeposit, Card card) {
        banknotesToDeposit.keySet().forEach(banknote -> {
            if (banknote.getCurrency() != card.getCurrency()) {
                try {
                    throw new ForbiddenOperation("Внесенные деньги не соответствуют валюте счета");
                } catch (ForbiddenOperation e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
