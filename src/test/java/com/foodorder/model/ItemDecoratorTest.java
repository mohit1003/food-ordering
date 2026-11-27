package com.foodorder.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ItemDecoratorTest {
    private final MenuItem burger = new MenuItem(1, "Burger", new BigDecimal("120.00"));

    @Test
    void baseItemKeepsItsNameAndPrice() {
        assertEquals("Burger", burger.name());
        assertEquals(new BigDecimal("120.00"), burger.price());
    }

    @Test
    void cheeseAddsItsNameAndPrice() {
        PricedItem item = new CheeseDecorator(burger);

        assertEquals("Burger + Cheese", item.name());
        assertEquals(new BigDecimal("140.00"), item.price());
    }

    @Test
    void extraPattyAddsItsNameAndPrice() {
        PricedItem item = new ExtraPattyDecorator(burger);

        assertEquals("Burger + Extra Patty", item.name());
        assertEquals(new BigDecimal("170.00"), item.price());
    }

    @Test
    void decoratorsCanBeStackedInEitherOrder() {
        PricedItem cheeseThenPatty = new ExtraPattyDecorator(new CheeseDecorator(burger));
        PricedItem pattyThenCheese = new CheeseDecorator(new ExtraPattyDecorator(burger));

        assertEquals("Burger + Cheese + Extra Patty", cheeseThenPatty.name());
        assertEquals("Burger + Extra Patty + Cheese", pattyThenCheese.name());
        assertEquals(new BigDecimal("190.00"), cheeseThenPatty.price());
        assertEquals(new BigDecimal("190.00"), pattyThenCheese.price());
    }

    @Test
    void decoratedItemPriceIsMultipliedByCartQuantity() {
        var cart = new Cart();
        cart.add(new ExtraPattyDecorator(new CheeseDecorator(burger)), 2);

        assertEquals(new BigDecimal("380.00"), cart.total());
    }
}
