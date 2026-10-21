package com.foodorder.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record Order(UUID id, List<CartItem> items, Instant createdAt) {
    public Order { items = List.copyOf(items); }

    public String status() { return "PLACED"; }

    public BigDecimal total() {
        return items.stream().map(CartItem::subtotal)
                .reduce(new BigDecimal("0.00"), BigDecimal::add);
    }
}
