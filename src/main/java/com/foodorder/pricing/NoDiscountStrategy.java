package com.foodorder.pricing;

import java.math.BigDecimal;

public class NoDiscountStrategy implements DiscountStrategy {


    @Override
    public BigDecimal calculateDiscount(BigDecimal subtotal) {
        if (subtotal == null || subtotal.signum() < 0) {
            throw new IllegalArgumentException("Subtotal cannot be null or negative");
        }
        return BigDecimal.ZERO;
    }
}
