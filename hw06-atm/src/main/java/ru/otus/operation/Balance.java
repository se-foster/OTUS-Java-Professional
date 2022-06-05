package ru.otus.operation;

import ru.otus.card.Card;

/**
 * Интерфейс позволяет узнать баланс на карте
 */
public interface Balance {
    String getBalance(Card card);
}
