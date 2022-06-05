package ru.otus.operation;

import java.util.Map;

import ru.otus.banknote.Banknote;
import ru.otus.card.Card;

/**
 * для банкоматов, которые умеют принимать наличные
 */
public interface Deposit {
     int deposit(Map<Banknote, Integer> banknotesToDeposit, Card card) throws ForbiddenOperation;
}
