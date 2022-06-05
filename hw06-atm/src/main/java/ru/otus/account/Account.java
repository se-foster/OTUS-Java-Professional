package ru.otus.account;

import ru.otus.Currency;
import ru.otus.operation.ForbiddenOperation;

public class Account {
    private final Currency currency;
    private double balance;

    public Account(Currency currency, double balance) {
        this.currency = currency;
        this.balance = balance;
    }

    public Currency getCurrency() {
        return currency;
    }

    public double getBalance() {
        return balance;
    }

    public int getMoney(int value, Currency currency) throws ForbiddenOperation {
        if (currency != this.currency) {
            throw new ForbiddenOperation("Валюта для снятия не соответствует валюте счета");
        }
        if (balance >= value) {
            balance -= value;
            return value;
        } else {
            throw new ForbiddenOperation("Недостаточно средств");
        }
    }

    public void insertMoney(int value, Currency currency) throws ForbiddenOperation {
        if (currency != this.currency) {
            throw new ForbiddenOperation("Валюта для пополнения не соответствует валюте счета");
        }
        balance += value;
    }
}
