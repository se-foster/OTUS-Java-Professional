package ru.otus.banknote;

import ru.otus.Currency;

public abstract class Banknote implements Comparable<Banknote> {

    @Override
    public int compareTo(Banknote banknote) {
        return banknote.getNominal() - nominal;
    }

    private final int nominal;
    private final Currency currency;

    public Banknote(int nominal, Currency currency) {
        this.nominal = nominal;
        this.currency = currency;
    }

    public int getNominal() {
        return nominal;
    }

    public Currency getCurrency() {
        return currency;
    }
}
