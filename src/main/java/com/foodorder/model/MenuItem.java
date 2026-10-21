package com.foodorder.model;

import java.math.BigDecimal;
import java.util.Objects;

public record MenuItem(int id, String name, BigDecimal price) {
    public MenuItem {
        Objects.requireNonNull(name);
        Objects.requireNonNull(price);
        if (id < 1 || name.isBlank() || price.signum() < 0) {
            throw new IllegalArgumentException("Invalid menu item");
        }
    }
}
