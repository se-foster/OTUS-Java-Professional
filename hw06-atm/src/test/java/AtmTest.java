import java.util.Map;

import org.junit.jupiter.api.Test;
import ru.otus.atm.AtmOnlyWithdrawals;
import ru.otus.atm.AtmWithDeposit;
import ru.otus.banknote.Banknote;
import ru.otus.banknote.Banknote100;
import ru.otus.banknote.Banknote1000;
import ru.otus.banknote.Banknote500;
import ru.otus.banknote.Banknote5000;
import ru.otus.operation.ForbiddenOperation;
import ru.otus.storage.Storage;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AtmTest {

    private static final Map<Banknote, Integer> BANKNOTES_IN_ATM = Map.of(
            new Banknote100(), 100, new Banknote500(), 50, new Banknote1000(), 10, new Banknote5000(), 2);
    private static final int BANKNOTES_IN_ATM_SUM = BANKNOTES_IN_ATM.entrySet().stream()
            .mapToInt(entry -> entry.getKey().getNominal() * entry.getValue()).sum(); // 55_000

    @Test
    void emptyStorageTest() {
        AtmOnlyWithdrawals atmOnlyWithdrawals = new AtmOnlyWithdrawals(new Storage(Map.of()));
        assertEquals(0, atmOnlyWithdrawals.getBalance());
        try {
            atmOnlyWithdrawals.withdrawal(60_000);
        } catch (ForbiddenOperation e) {
            assertEquals("Максимально доступная сумма для снятия в этом банкомате 0", e.getMessage());
        }
    }

    @Test
    void notEmptyStorageButBigSumToCashTest() {
        AtmOnlyWithdrawals atmOnlyWithdrawals = new AtmOnlyWithdrawals(new Storage(BANKNOTES_IN_ATM));
        assertEquals(BANKNOTES_IN_ATM_SUM, atmOnlyWithdrawals.getBalance());
        try {
            atmOnlyWithdrawals.withdrawal(60_000);
        } catch (ForbiddenOperation e) {
            assertEquals("Максимально доступная сумма для снятия в этом банкомате 55000", e.getMessage());
        }
    }

    @Test
    void withdrawalTest() throws ForbiddenOperation {
        AtmWithDeposit atmWithDeposit = new AtmWithDeposit(new Storage(BANKNOTES_IN_ATM));
        Map<Banknote, Integer> money = atmWithDeposit.withdrawal(9900);
        assertEquals(4, money.size());
        assertEquals(1, money.get(new Banknote5000()));
        assertEquals(4, money.get(new Banknote1000()));
        assertEquals(1, money.get(new Banknote500()));
        assertEquals(4, money.get(new Banknote100()));
        assertEquals(BANKNOTES_IN_ATM_SUM - 9900, atmWithDeposit.getBalance());
    }

    @Test
    void depositTest() {
        AtmWithDeposit atmWithDeposit = new AtmWithDeposit(new Storage(BANKNOTES_IN_ATM));
        atmWithDeposit.deposit(Map.of(new Banknote500(), 10, new Banknote1000(), 5));
        assertEquals(BANKNOTES_IN_ATM_SUM + 10_000, atmWithDeposit.getBalance()) ;
    }

    @Test
    void severalOperationsTest() throws ForbiddenOperation {
        int banknotesInAtmSum = BANKNOTES_IN_ATM_SUM;
        AtmWithDeposit atmWithDeposit = new AtmWithDeposit(new Storage(BANKNOTES_IN_ATM));
        assertEquals(banknotesInAtmSum, atmWithDeposit.getBalance());
        Map<Banknote, Integer> money = atmWithDeposit.withdrawal(10_000);
        assertEquals(1, money.size());
        assertEquals(2, money.get(new Banknote5000()));
        assertEquals(banknotesInAtmSum -= 10_000, atmWithDeposit.getBalance());
        money = atmWithDeposit.withdrawal(10_000);
        assertEquals(1, money.size());
        assertEquals(10, money.get(new Banknote1000()));
        assertEquals(banknotesInAtmSum -= 10_000, atmWithDeposit.getBalance());
        atmWithDeposit.deposit(Map.of(new Banknote5000(), 1));
        assertEquals(banknotesInAtmSum += 5000, atmWithDeposit.getBalance());
        money = atmWithDeposit.withdrawal(10_000);
        assertEquals(2, money.size());
        assertEquals(1, money.get(new Banknote5000()));
        assertEquals(10, money.get(new Banknote500()));
        assertEquals(banknotesInAtmSum - 10_000, atmWithDeposit.getBalance());
    }
}
