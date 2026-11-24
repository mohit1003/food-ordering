package com.foodorder.pricing;


import java.math.BigDecimal;

public class FlatDiscountStrategy implements DiscountStrategy {

    private final BigDecimal discount;

    public FlatDiscountStrategy(BigDecimal discount) {
        if (discount == null || discount.signum() < 0) {
            throw new IllegalArgumentException(
                    "Discount cannot be negative"
            );
        }

        this.discount = discount;
    }

    @Override
    public BigDecimal calculateDiscount(BigDecimal subtotal) {
        if (subtotal == null || subtotal.signum() < 0) {
            throw new IllegalArgumentException("Subtotal cannot be null or negative");
        }
        return discount.min(subtotal);
    }
}
