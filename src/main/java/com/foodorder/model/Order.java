package com.foodorder.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record Order(UUID id, List<CartItem> items, Instant createdAt, BigDecimal discount) {
    public Order(UUID id, List<CartItem> items, Instant createdAt) {
        this(id, items, createdAt, BigDecimal.ZERO);
    }

    public Order {
        items = List.copyOf(items);
        if (discount == null || discount.signum() < 0) {
            throw new IllegalArgumentException("Discount cannot be null or negative");
        }
        if (discount.compareTo(calculateSubtotal(items)) > 0) {
            throw new IllegalArgumentException("Discount cannot exceed the order subtotal");
        }
    }

    public String status() { return "PLACED"; }

    public BigDecimal total() {
        return subtotal().subtract(discount);
    }

    public BigDecimal subtotal() {
        return calculateSubtotal(items);
    }

    private static BigDecimal calculateSubtotal(List<CartItem> items) {
        return items.stream().map(CartItem::subtotal)
                .reduce(new BigDecimal("0.00"), BigDecimal::add);
    }
}
