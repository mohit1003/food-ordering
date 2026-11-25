package com.foodorder.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public record MenuItem(int id, String name, BigDecimal price, List<MenuAddOn> availableAddOns)
        implements PricedItem {
    public MenuItem(int id, String name, BigDecimal price) {
        this(id, name, price, List.of());
    }

    public MenuItem {
        Objects.requireNonNull(name);
        Objects.requireNonNull(price);
        availableAddOns = List.copyOf(availableAddOns);
        if (id < 1 || name.isBlank() || price.signum() < 0) {
            throw new IllegalArgumentException("Invalid menu item");
        }
    }
}
