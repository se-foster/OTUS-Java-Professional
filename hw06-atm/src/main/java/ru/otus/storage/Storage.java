package ru.otus.storage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.otus.banknote.Banknote;
import ru.otus.operation.ForbiddenOperation;

/**
 * хранилище с деньгами
 */
public class Storage {

    private Map<Banknote, Integer> banknotesCountMap;

    public Storage(Map<Banknote, Integer> banknotesCountMap) {
        this.banknotesCountMap = new HashMap<>(banknotesCountMap);
    }

    /**
     * Заполняем банкомат купюрами
     *
     * @param banknote тип купюры
     * @param count    количество купюр
     */
    public void addBanknotes(Banknote banknote, int count) {
        banknotesCountMap.compute(banknote, (k, v) -> v != null ? v + count : count);
    }

    public Map<Banknote, Integer> getMoney(int amount) throws ForbiddenOperation {
        if (getBalance() < amount) {
            throw new ForbiddenOperation("Максимально доступная сумма для снятия в этом банкомате " + getBalance());
        }
        int resultAmount = 0;
        Map<Banknote, Integer> banknotesToCash = new HashMap<>();
        // работаем с копией на случай, если собрать сумму не получится
        Map<Banknote, Integer> banknotesCountMapCopy = new HashMap<>(banknotesCountMap);
        for (Banknote banknote : getAvailableBanknotes()) {
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

    private List<Banknote> getAvailableBanknotes() {
        return banknotesCountMap.keySet().stream().sorted().toList();
    }

    public int getBalance() {
        return banknotesCountMap.entrySet().stream()
                .mapToInt(entry -> entry.getKey().getNominal() * entry.getValue())
                .sum();
    }
}
