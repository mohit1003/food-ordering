package com.foodorder.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record Order(UUID id, List<CartItem> items, Instant createdAt, BigDecimal discount,
                    String customerName, String contactPerson, String deliveryInstructions) {
    public Order(UUID id, List<CartItem> items, Instant createdAt) {
        this(id, items, createdAt, BigDecimal.ZERO, null, null, null);
    }

    public Order(UUID id, List<CartItem> items, Instant createdAt, BigDecimal discount) {
        this(id, items, createdAt, discount, null, null, null);
    }

    public Order {
        if (id == null) {
            throw new IllegalArgumentException("Order ID is required");
        }
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }
        if (createdAt == null) {
            throw new IllegalArgumentException("Order creation time is required");
        }
        items = List.copyOf(items);
        if (discount == null || discount.signum() < 0) {
            throw new IllegalArgumentException("Discount cannot be null or negative");
        }
        if (discount.compareTo(calculateSubtotal(items)) > 0) {
            throw new IllegalArgumentException("Discount cannot exceed the order subtotal");
        }
        if (customerName != null && customerName.isBlank()) {
            throw new IllegalArgumentException("Customer name cannot be blank");
        }
        if (contactPerson != null && contactPerson.isBlank()) {
            throw new IllegalArgumentException("Contact person cannot be blank");
        }
        if (deliveryInstructions != null && deliveryInstructions.isBlank()) {
            throw new IllegalArgumentException("Delivery instructions cannot be blank");
        }
    }

    public String status() { return "PLACED"; }

    public BigDecimal total() {
        return subtotal().subtract(discount);
    }

    public BigDecimal subtotal() {
        return calculateSubtotal(items);
    }

    private static BigDecimal calculateSubtotal(List<CartItem> items) {
        return items.stream().map(CartItem::subtotal)
                .reduce(new BigDecimal("0.00"), BigDecimal::add);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private UUID id;
        private List<CartItem> items;
        private Instant createdAt;
        private BigDecimal discount = BigDecimal.ZERO;
        private String customerName;
        private String contactPerson;
        private String deliveryInstructions;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder items(List<CartItem> items) {
            this.items = items;
            return this;
        }

        public Builder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder discount(BigDecimal discount) {
            this.discount = discount;
            return this;
        }

        public Builder customerName(String customerName) {
            this.customerName = customerName;
            return this;
        }

        public Builder contactPerson(String contactPerson) {
            this.contactPerson = contactPerson;
            return this;
        }

        public Builder deliveryInstructions(String deliveryInstructions) {
            this.deliveryInstructions = deliveryInstructions;
            return this;
        }

        public Order build() {
            return new Order(
                    id, items, createdAt, discount,
                    customerName, contactPerson, deliveryInstructions
            );
        }

    }
}
