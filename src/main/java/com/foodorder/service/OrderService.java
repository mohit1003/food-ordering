package com.foodorder.service;

import com.foodorder.model.*;
import com.foodorder.pricing.DiscountStrategy;
import com.foodorder.pricing.NoDiscountStrategy;
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
        return placeOrder(cart, new NoDiscountStrategy());
    }

    public Order placeOrder(Cart cart, DiscountStrategy discountStrategy) {
        if (cart.isEmpty()) throw new IllegalArgumentException("Your cart is empty");
        Objects.requireNonNull(discountStrategy, "Discount strategy is required");
        BigDecimal subtotal = cart.total();
        BigDecimal discount = discountStrategy.calculateDiscount(subtotal);
        if (discount == null || discount.signum() < 0 || discount.compareTo(subtotal) > 0) {
            throw new IllegalArgumentException("Discount strategy returned an invalid discount");
        }
        Order order = new Order(UUID.randomUUID(), cart.items(), Instant.now(), discount);
        orders.put(order.id(), order);
        cart.clear();
        return order;
    }

    public Optional<Order> findOrder(UUID id) {
        return Optional.ofNullable(orders.get(id));
    }
}
