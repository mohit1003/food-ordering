package com.foodorder.payment;

public class MockCardProcessor {
    private final boolean paymentsEnabled;

    public MockCardProcessor(boolean paymentsEnabled) {
        this.paymentsEnabled = paymentsEnabled;
    }

    public boolean authorize(int amountInPaise) {
        return paymentsEnabled && amountInPaise > 0;
    }
}
