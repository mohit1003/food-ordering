package com.foodorder.payment;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class WalletProviderAdapter implements PaymentGateway {
    private final MockWalletClient mockWalletClient;

    public WalletProviderAdapter(MockWalletClient mockWalletClient) {
        this.mockWalletClient = Objects.requireNonNull(mockWalletClient, "Wallet client is required");
    }

    @Override
    public PaymentResult charge(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("Payment amount must be positive");
        }
        String amountString;
        try {
            amountString = amount.setScale(2, RoundingMode.UNNECESSARY).toPlainString();
        } catch (ArithmeticException e) {
            throw new IllegalArgumentException("Payment amount cannot have fractions smaller than one paisa", e);
        }
        boolean approved = "SUCCESS".equals(mockWalletClient.sendMoney("INR", amountString));
        return new PaymentResult(approved,
                approved ? "Wallet payment approved" : "Wallet payment declined");
    }
}
