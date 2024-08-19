package core.basesyntax.strategy.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import core.basesyntax.db.Storage;
import core.basesyntax.model.FruitTransaction;
import core.basesyntax.strategy.OperationHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class PurchaseOperationTest {
    private static final String APPLE = "apple";
    private static final int DEFAULT_QUANTITY = 10;
    private static final int PART_OF_DEFAULT_QUANTITY = 1;
    private static final int ZERO_QUANTITY = 0;
    private static final int NEGATIVE_QUANTITY = -1;
    private static OperationHandler operationHandler;

    @BeforeAll
    static void beforeAll() {
        operationHandler = new PurchaseOperation();
    }

    @AfterEach
    void afterEach() {
        Storage.clear();
    }

    @Test
    void apply_purchaseAllProduct_ok() {
        Storage.getFruits().put(APPLE, DEFAULT_QUANTITY);
        FruitTransaction fruitTransaction = new FruitTransaction(
                FruitTransaction.Operation.BALANCE, APPLE, DEFAULT_QUANTITY);
        operationHandler.apply(fruitTransaction);
        int actual = Storage.getFruits().get(APPLE);
        assertEquals(ZERO_QUANTITY, actual);
    }

    @Test
    void apply_purchasePartProduct_ok() {
        Storage.getFruits().put(APPLE, DEFAULT_QUANTITY);
        FruitTransaction fruitTransaction = new FruitTransaction(
                FruitTransaction.Operation.BALANCE, APPLE, PART_OF_DEFAULT_QUANTITY);
        operationHandler.apply(fruitTransaction);
        int actual = Storage.getFruits().get(APPLE);
        assertEquals(DEFAULT_QUANTITY - PART_OF_DEFAULT_QUANTITY, actual);
    }

    @Test
    void apply_purchaseInputMoreThanCount_notOk() {
        Storage.getFruits().put(APPLE, ZERO_QUANTITY);
        FruitTransaction fruitTransaction = new FruitTransaction(
                FruitTransaction.Operation.BALANCE, APPLE, DEFAULT_QUANTITY);
        assertThrows(IllegalArgumentException.class, () -> operationHandler
                .apply(fruitTransaction));
    }

    @Test
    void apply_purchaseZeroProducts_notOk() {
        Storage.getFruits().put(APPLE, DEFAULT_QUANTITY);
        FruitTransaction fruitTransaction = new FruitTransaction(
                FruitTransaction.Operation.BALANCE, APPLE, ZERO_QUANTITY);
        assertThrows(IllegalArgumentException.class, () -> operationHandler
                .apply(fruitTransaction));
    }

    @Test
    void apply_putNegQuantity_notOK() {
        FruitTransaction fruitTransaction = new FruitTransaction(
                FruitTransaction.Operation.BALANCE, APPLE, NEGATIVE_QUANTITY);
        assertThrows(IllegalArgumentException.class, () -> operationHandler
                .apply(fruitTransaction));
    }
}
