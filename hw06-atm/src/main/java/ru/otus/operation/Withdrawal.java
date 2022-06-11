package ru.otus.operation;

import java.util.Map;

import ru.otus.banknote.Banknote;

/**
 * снятие наличных
 */
public interface Withdrawal {
    Map<Banknote, Integer> withdrawal(int amount) throws ForbiddenOperation;
}
