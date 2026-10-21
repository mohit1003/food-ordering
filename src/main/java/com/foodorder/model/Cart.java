package com.foodorder.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Cart {
    private final List<CartItem> items = new ArrayList<>();

    public void add(MenuItem item, int quantity) {
        items.add(new CartItem(item, quantity));
    }

    public List<CartItem> items() { return List.copyOf(items); }
    public boolean isEmpty() { return items.isEmpty(); }
    public void clear() { items.clear(); }

    public BigDecimal total() {
        return items.stream().map(CartItem::subtotal)
                .reduce(new BigDecimal("0.00"), BigDecimal::add);
    }
}
