package com.foodorder.pricing;

import java.math.BigDecimal;

public class NoDiscountStrategy implements DiscountStrategy {


    @Override
    public BigDecimal calculateDiscount(BigDecimal Subtotal) {
        return BigDecimal.ZERO;
    }
}
