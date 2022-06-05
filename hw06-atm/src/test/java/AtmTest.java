import java.util.Map;

import org.junit.jupiter.api.Test;
import ru.otus.account.Account;
import ru.otus.atm.AtmOnlyWithdrawals;
import ru.otus.atm.AtmWithDeposit;
import ru.otus.banknote.Banknote;
import ru.otus.banknote.Dollar100;
import ru.otus.banknote.Dollar50;
import ru.otus.banknote.Euro100;
import ru.otus.banknote.Euro50;
import ru.otus.banknote.Ruble100;
import ru.otus.banknote.Ruble1000;
import ru.otus.banknote.Ruble500;
import ru.otus.banknote.Ruble5000;
import ru.otus.card.Card;
import ru.otus.operation.ForbiddenOperation;
import ru.otus.storage.Storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.otus.Currency.RUB;
import static ru.otus.Currency.USD;

public class AtmTest {

    private static final Map<Banknote, Integer> RUB_BANKNOTES = Map.of( // 55000 RUB
            new Ruble100(), 100, new Ruble500(), 50, new Ruble1000(), 10, new Ruble5000(), 2);
    private static final Map<Banknote, Integer> EUR_AND_USD_BANKNOTES = Map.of( // 1000 USD, 1000 EUR
            new Dollar50(), 10, new Dollar100(), 5, new Euro50(), 20, new Euro100(), 10);

    @Test
    void emptyStorageTest() {
        AtmOnlyWithdrawals atmOnlyWithdrawals = new AtmOnlyWithdrawals(new Storage(Map.of()));
        try {
            atmOnlyWithdrawals.withdrawal(1000, new Card(new Account(RUB, 10_000)));
        } catch (ForbiddenOperation e) {
            assertEquals("Максимально доступная сумма для снятия в этом банкомате 0 RUB", e.getMessage());
        }
    }

    @Test
    void withdrawalRoubleTest() throws ForbiddenOperation {
        Card card = new Card(new Account(RUB, 10_000));
        AtmWithDeposit atmWithDeposit = new AtmWithDeposit(new Storage(RUB_BANKNOTES));
        Map<Banknote, Integer> money = atmWithDeposit.withdrawal(9900, card);
        assertEquals(4, money.size());
        assertEquals(1, money.get(new Ruble5000()));
        assertEquals(4, money.get(new Ruble1000()));
        assertEquals(1, money.get(new Ruble500()));
        assertEquals(4, money.get(new Ruble100()));
        assertEquals(100, card.getBalance());
    }

    @Test
    void notEnoughMoneyOnAccountTest() {
        AtmWithDeposit atmWithDeposit = new AtmWithDeposit(new Storage(RUB_BANKNOTES));
        try {
            atmWithDeposit.withdrawal(1_000_000, new Card(new Account(RUB, 10_000)));
        } catch (ForbiddenOperation e) {
            assertEquals("Недостаточно средств", e.getMessage());
        }
    }

    @Test
    void notEnoughMoneyInAtmUsdTest() {
        AtmWithDeposit atmWithDeposit = new AtmWithDeposit(new Storage(EUR_AND_USD_BANKNOTES));
        try {
            atmWithDeposit.withdrawal(10_000, new Card(new Account(USD, 15_000)));
        } catch (ForbiddenOperation e) {
            assertEquals("Максимально доступная сумма для снятия в этом банкомате 1000 USD", e.getMessage());
        }
    }

    @Test
    void severalOperationsTest() throws ForbiddenOperation {
        Card card = new Card(new Account(RUB, 100_000));
        AtmWithDeposit atmWithDeposit = new AtmWithDeposit(new Storage(RUB_BANKNOTES));
        Map<Banknote, Integer> money = atmWithDeposit.withdrawal(10_000, card);
        assertEquals(1, money.size());
        assertEquals(2, money.get(new Ruble5000()));
        assertEquals(90_000, card.getBalance());
        money = atmWithDeposit.withdrawal(10_000, card);
        assertEquals(1, money.size());
        assertEquals(10, money.get(new Ruble1000()));
        assertEquals(80_000, card.getBalance());
        atmWithDeposit.deposit(Map.of(new Ruble5000(), 1), card);
        assertEquals(85_000, card.getBalance());
        money = atmWithDeposit.withdrawal(10_000, card);
        assertEquals(2, money.size());
        assertEquals(1, money.get(new Ruble5000()));
        assertEquals(10, money.get(new Ruble500()));
        assertEquals(75_000, card.getBalance());
    }
}
