package com.foodorder.model;

import java.math.BigDecimal;
import java.util.Objects;

public record CheeseDecorator(PricedItem wrappedItem) implements PricedItem {
    private static final BigDecimal PRICE = new BigDecimal("20.00");

    public CheeseDecorator {
        Objects.requireNonNull(wrappedItem, "Wrapped item is required");
    }

    @Override
    public String name() {
        return wrappedItem.name() + " + Cheese";
    }

    @Override
    public BigDecimal price() {
        return wrappedItem.price().add(PRICE);
    }
}
