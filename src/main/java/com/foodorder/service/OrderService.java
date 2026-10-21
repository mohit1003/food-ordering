package com.foodorder.service;

import com.foodorder.model.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

public class OrderService {
    private final List<MenuItem> menu = List.of(
            new MenuItem(1, "Burger", new BigDecimal("120.00")),
            new MenuItem(2, "Pizza", new BigDecimal("250.00")),
            new MenuItem(3, "Fries", new BigDecimal("80.00")),
            new MenuItem(4, "Sandwich", new BigDecimal("100.00")),
            new MenuItem(5, "Lemonade", new BigDecimal("50.00")));
    private final Map<UUID, Order> orders = new LinkedHashMap<>();

    public List<MenuItem> menu() { return menu; }

    public MenuItem findMenuItem(int id) {
        return menu.stream().filter(item -> item.id() == id).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Menu item not found"));
    }

    public Order placeOrder(Cart cart) {
        if (cart.isEmpty()) throw new IllegalArgumentException("Your cart is empty");
        Order order = new Order(UUID.randomUUID(), cart.items(), Instant.now());
        orders.put(order.id(), order);
        cart.clear();
        return order;
    }

    public Optional<Order> findOrder(UUID id) {
        return Optional.ofNullable(orders.get(id));
    }
}
