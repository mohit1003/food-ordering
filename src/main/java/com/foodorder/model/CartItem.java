package com.foodorder.model;

import java.math.BigDecimal;
import java.util.Objects;

public record CartItem(MenuItem item, int quantity) {
    public CartItem {
        Objects.requireNonNull(item);
        if (quantity < 1) throw new IllegalArgumentException("Quantity must be positive");
    }

    public BigDecimal subtotal() {
        return item.price().multiply(BigDecimal.valueOf(quantity));
    }
}
