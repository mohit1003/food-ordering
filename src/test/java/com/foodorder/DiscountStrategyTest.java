package com.foodorder;

import com.foodorder.model.Cart;
import com.foodorder.model.CheeseDecorator;
import com.foodorder.pricing.DiscountStrategy;
import com.foodorder.pricing.FlatDiscountStrategy;
import com.foodorder.pricing.NoDiscountStrategy;
import com.foodorder.pricing.PercentageDiscountStrategy;
import com.foodorder.service.OrderService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DiscountStrategyTest {
    @Test
    void percentageDiscountRoundsHalfUpToTwoDecimalPlaces() {
        var strategy = new PercentageDiscountStrategy(new BigDecimal("10"));

        assertEquals(new BigDecimal("1.24"), strategy.calculateDiscount(new BigDecimal("12.35")));
        assertEquals(new BigDecimal("0.00"), strategy.calculateDiscount(new BigDecimal("0.00")));
    }

    @Test
    void flatDiscountCannotExceedSubtotal() {
        var strategy = new FlatDiscountStrategy(new BigDecimal("100"));

        assertEquals(new BigDecimal("25.00"), strategy.calculateDiscount(new BigDecimal("25.00")));
    }

    @Test
    void noDiscountReturnsZero() {
        assertEquals(BigDecimal.ZERO,
                new NoDiscountStrategy().calculateDiscount(new BigDecimal("25.00")));
    }

    @Test
    void strategiesRejectNullOrNegativeSubtotals() {
        List<DiscountStrategy> strategies = List.of(
                new NoDiscountStrategy(),
                new PercentageDiscountStrategy(new BigDecimal("10")),
                new FlatDiscountStrategy(new BigDecimal("5"))
        );

        for (DiscountStrategy strategy : strategies) {
            assertThrows(IllegalArgumentException.class, () -> strategy.calculateDiscount(null));
            assertThrows(IllegalArgumentException.class,
                    () -> strategy.calculateDiscount(new BigDecimal("-1")));
        }
    }

    @Test
    void checkoutAppliesAndStoresDiscount() {
        var service = new OrderService();
        var cart = new Cart();
        cart.add(service.findMenuItem(1), 1); // INR 120

        var order = service.placeOrder(cart, new PercentageDiscountStrategy(new BigDecimal("10")));

        assertEquals(new BigDecimal("120.00"), order.subtotal());
        assertEquals(new BigDecimal("12.00"), order.discount());
        assertEquals(new BigDecimal("108.00"), order.total());
        assertTrue(cart.isEmpty());
    }

    @Test
    void checkoutDiscountAppliesAfterItemDecorators() {
        var service = new OrderService();
        var cart = new Cart();
        cart.add(new CheeseDecorator(service.findMenuItem(1)), 1);

        var order = service.placeOrder(cart, new PercentageDiscountStrategy(new BigDecimal("10")));

        assertEquals("Burger + Cheese", order.items().getFirst().item().name());
        assertEquals(new BigDecimal("140.00"), order.subtotal());
        assertEquals(new BigDecimal("14.00"), order.discount());
        assertEquals(new BigDecimal("126.00"), order.total());
    }
}
