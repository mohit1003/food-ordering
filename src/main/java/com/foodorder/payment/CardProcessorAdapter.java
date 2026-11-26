package com.foodorder.payment;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class CardProcessorAdapter implements PaymentGateway {
    private final MockCardProcessor cardProcessor;

    public CardProcessorAdapter(MockCardProcessor cardProcessor) {
        this.cardProcessor = Objects.requireNonNull(cardProcessor, "Card processor is required");
    }

    @Override
    public PaymentResult charge(BigDecimal amount) {
        BigDecimal validAmount = validatedAmount(amount);
        int amountInPaise;
        try {
            amountInPaise = validAmount.movePointRight(2).intValueExact();
        } catch (ArithmeticException e) {
            throw new IllegalArgumentException("Payment amount is too large for the card processor", e);
        }
        boolean approved = cardProcessor.authorize(amountInPaise);
        return new PaymentResult(approved,
                approved ? "Card payment approved" : "Card payment declined");
    }

    private static BigDecimal validatedAmount(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("Payment amount must be positive");
        }
        try {
            return amount.setScale(2, RoundingMode.UNNECESSARY);
        } catch (ArithmeticException e) {
            throw new IllegalArgumentException("Payment amount cannot have fractions smaller than one paisa", e);
        }
    }
}
