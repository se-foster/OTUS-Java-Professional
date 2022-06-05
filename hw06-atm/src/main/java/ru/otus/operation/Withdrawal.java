package ru.otus.operation;

import java.util.Map;

import ru.otus.banknote.Banknote;
import ru.otus.card.Card;

/**
 * снятие наличных
 */
public interface Withdrawal {
    Map<Banknote, Integer> withdrawal(int amount, Card card) throws ForbiddenOperation;
}
