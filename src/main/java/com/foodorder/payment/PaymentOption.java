package com.foodorder.payment;

import java.util.Objects;

public record PaymentOption(String name, PaymentGateway gateway) {
    public PaymentOption {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Payment option name is required");
        }
        Objects.requireNonNull(gateway, "Payment gateway is required");
    }
}
