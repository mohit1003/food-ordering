package com.foodorder.model;

import java.math.BigDecimal;
import java.util.Objects;

public record ExtraPattyDecorator(PricedItem wrappedItem) implements PricedItem {
    private static final BigDecimal PRICE = new BigDecimal("50.00");

    public ExtraPattyDecorator {
        Objects.requireNonNull(wrappedItem, "Wrapped item is required");
    }

    @Override
    public String name() {
        return wrappedItem.name() + " + Extra Patty";
    }

    @Override
    public BigDecimal price() {
        return wrappedItem.price().add(PRICE);
    }
}
