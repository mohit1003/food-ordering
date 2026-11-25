package com.foodorder.model;

import java.math.BigDecimal;
import java.util.Objects;

public record ExtraPeriPeriDecorator(PricedItem wrappedItem) implements PricedItem {
    private static final BigDecimal PRICE = new BigDecimal("05.00");
    public ExtraPeriPeriDecorator{
        Objects.requireNonNull(wrappedItem, "Wrapped item is required");
    }

    @Override
    public String name() {
        return wrappedItem.name() + " Peri Peri Masala";
    }

    @Override
    public BigDecimal price() {
        return wrappedItem.price().add(PRICE);
    }
}
