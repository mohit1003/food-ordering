package com.foodorder;

import com.foodorder.model.Cart;
import com.foodorder.service.OrderService;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class OrderServiceTest {
    @Test void checkoutPreservesOrderAndClearsCart() {
        var service = new OrderService();
        var cart = new Cart();
        cart.add(service.findMenuItem(1), 2);
        var order = service.placeOrder(cart);
        assertEquals(new BigDecimal("240.00"), order.total());
        assertTrue(cart.isEmpty());
        cart.add(service.findMenuItem(2), 1);
        assertEquals(1, order.items().size());
        assertEquals(order, service.findOrder(order.id()).orElseThrow());
    }

    @Test void emptyCheckoutAndInvalidQuantityAreRejected() {
        var service = new OrderService();
        var cart = new Cart();
        assertThrows(IllegalArgumentException.class, () -> service.placeOrder(cart));
        assertThrows(IllegalArgumentException.class, () -> cart.add(service.findMenuItem(1), 0));
    }
}
