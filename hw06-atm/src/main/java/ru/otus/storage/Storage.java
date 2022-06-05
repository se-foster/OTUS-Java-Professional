package ru.otus.storage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import ru.otus.Currency;
import ru.otus.banknote.Banknote;
import ru.otus.operation.ForbiddenOperation;

/**
 * хранилище с деньгами
 */
public class Storage {
    private Map<Banknote, Integer> banknotesCountMap;
    private final Map<Currency, Long> currencyBalanceMap = new HashMap<>();
    public Storage(Map<Banknote, Integer> banknotesCountMap) {
        this.banknotesCountMap = new HashMap<>(banknotesCountMap);
        reloadCurrencyBalanceMap();
    }

    private void reloadCurrencyBalanceMap() {
        currencyBalanceMap.clear();
        banknotesCountMap.forEach((banknote, count) -> {
                    long sum = (long) banknote.getNominal() * count;
                    currencyBalanceMap.compute(banknote.getCurrency(), (k, v) -> v != null ? v + sum : sum);
                }
        );
    }

    /**
     * Заполняем банкомат купюрами
     *
     * @param banknote тип купюры
     * @param count    количество купюр
     */
    public int addBanknotes(Banknote banknote, int count) {
        banknotesCountMap.compute(banknote, (k, v) -> v != null ? v + count : count);
        reloadCurrencyBalanceMap();
        return banknote.getNominal() * count;
    }

    /**
     * @return какие на данный момент есть купюры выбранной валюты (отсортировано по убыванию номинала)
     */
    public List<Banknote> getAvailableBanknotes(Currency currency) {
        return banknotesCountMap.keySet().stream()
                .filter(banknote -> banknote.getCurrency() == currency)
                .filter(banknote -> banknotesCountMap.get(banknote) > 0)
                .sorted()
                .toList();
    }

    public Map<Banknote, Integer> getMoney(int amount, Currency currency) throws ForbiddenOperation {
        Map<Banknote, Integer> result = checkAndCollect(amount, currency);
        reloadCurrencyBalanceMap();
        return result;
    }

    private Map<Banknote, Integer> checkAndCollect(int amount, Currency currency) throws ForbiddenOperation {
        if (currencyBalanceMap.getOrDefault(currency, 0L) < amount) {
            throw new ForbiddenOperation("Максимально доступная сумма для снятия в этом банкомате "
                    + currencyBalanceMap.getOrDefault(currency, 0L) + " " + currency.name());
        }
        int resultAmount = 0;
        Map<Banknote, Integer> banknotesToCash = new TreeMap<>();
        // работаем с копией на случай, если собрать сумму не получится
        Map<Banknote, Integer> banknotesCountMapCopy = new HashMap<>(banknotesCountMap);
        for (Banknote banknote : getAvailableBanknotes(currency)) {
            while ((amount - resultAmount) / banknote.getNominal() != 0 && banknotesCountMapCopy.get(banknote) > 0 &&
                    resultAmount < amount) {
                banknotesCountMapCopy.compute(banknote, (k, v) -> --v);
                resultAmount += banknote.getNominal();
                banknotesToCash.compute(banknote, (k, v) -> v != null ? ++v : 1);
            }
        }
        if (amount != resultAmount) {
            throw new ForbiddenOperation("Невозможно выдать заказанную сумму");
        }
        banknotesCountMap = banknotesCountMapCopy;
        return banknotesToCash;
    }
}
