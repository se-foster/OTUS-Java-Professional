package ru.otus.operation;

import java.util.Map;

import ru.otus.banknote.Banknote;

/**
 * для банкоматов, которые умеют принимать наличные
 */
public interface Deposit {
     void deposit(Map<Banknote, Integer> banknotesToDeposit);
}
