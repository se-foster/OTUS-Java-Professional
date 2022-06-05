package ru.otus.card;

import ru.otus.Currency;
import ru.otus.account.Account;
import ru.otus.operation.ForbiddenOperation;

public class Card {
    private final Account account;

    public Card(Account account) {
        this.account = account;
    }

    public double getBalance() {
        return account.getBalance();
    }

    public Currency getCurrency() {
        return account.getCurrency();
    }

    public int getMoney(int value, Currency currency) throws ForbiddenOperation {
        return account.getMoney(value, currency);
    }

    public void insertMoney(int value, Currency currency) throws ForbiddenOperation {
        account.insertMoney(value, currency);
    }
}
