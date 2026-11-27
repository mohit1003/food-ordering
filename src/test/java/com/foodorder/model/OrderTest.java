package com.foodorder.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {
    private static final Instant CREATED_AT = Instant.parse("2025-01-01T00:00:00Z");

    @Test
    void threeArgumentConstructorUsesZeroDiscount() {
        var order = new Order(UUID.randomUUID(), List.of(cartItem(2)), CREATED_AT);

        assertEquals(new BigDecimal("20.00"), order.subtotal());
        assertEquals(BigDecimal.ZERO, order.discount());
        assertEquals(new BigDecimal("20.00"), order.total());
        assertEquals("PLACED", order.status());
    }

    @Test
    void fourArgumentConstructorStoresDiscountAndComputesTotal() {
        var order = new Order(UUID.randomUUID(), List.of(cartItem(2)), CREATED_AT,
                new BigDecimal("5.00"));

        assertEquals(new BigDecimal("20.00"), order.subtotal());
        assertEquals(new BigDecimal("5.00"), order.discount());
        assertEquals(new BigDecimal("15.00"), order.total());
    }

    @Test
    void builderUsesZeroDiscountAndStoresOptionalDetails() {
        var order = Order.builder()
                .id(UUID.randomUUID())
                .items(List.of(cartItem(1)))
                .createdAt(CREATED_AT)
                .customerName("Asha")
                .contactPerson("555-0100")
                .deliveryInstructions("Leave at reception")
                .build();

        assertEquals(BigDecimal.ZERO, order.discount());
        assertEquals("Asha", order.customerName());
        assertEquals("555-0100", order.contactPerson());
        assertEquals("Leave at reception", order.deliveryInstructions());
    }

    @Test
    void builderRejectsMissingRequiredDataAndBlankOptionalDetails() {
        assertThrows(IllegalArgumentException.class, () -> Order.builder().build());

        assertThrows(IllegalArgumentException.class, () -> Order.builder()
                .id(UUID.randomUUID())
                .items(List.of(cartItem(1)))
                .createdAt(CREATED_AT)
                .customerName(" ")
                .build());
    }

    @Test
    void orderKeepsAnImmutableSnapshotOfItems() {
        var sourceItems = new ArrayList<>(List.of(cartItem(1)));
        var order = new Order(UUID.randomUUID(), sourceItems, CREATED_AT, BigDecimal.ZERO);

        sourceItems.clear();

        assertEquals(1, order.items().size());
        assertEquals(new BigDecimal("10.00"), order.total());
        assertThrows(UnsupportedOperationException.class, () -> order.items().clear());
    }

    @Test
    void rejectsMissingRequiredOrderData() {
        var id = UUID.randomUUID();
        var items = List.of(cartItem(1));

        assertThrows(IllegalArgumentException.class,
                () -> new Order(id, List.of(), CREATED_AT, BigDecimal.ZERO));
        assertThrows(IllegalArgumentException.class,
                () -> new Order(null, items, CREATED_AT, BigDecimal.ZERO));
        assertThrows(IllegalArgumentException.class,
                () -> new Order(id, items, null, BigDecimal.ZERO));
    }

    @Test
    void rejectsInvalidDiscountsAndNullItems() {
        var id = UUID.randomUUID();
        var items = List.of(cartItem(1));

        assertThrows(IllegalArgumentException.class,
                () -> new Order(id, items, CREATED_AT, null));
        assertThrows(IllegalArgumentException.class,
                () -> new Order(id, items, CREATED_AT, new BigDecimal("-1.00")));
        assertThrows(IllegalArgumentException.class,
                () -> new Order(id, items, CREATED_AT, new BigDecimal("10.01")));
        assertThrows(IllegalArgumentException.class,
                () -> new Order(id, null, CREATED_AT, BigDecimal.ZERO));
    }

    private static CartItem cartItem(int quantity) {
        return new CartItem(new MenuItem(1, "Test item", new BigDecimal("10.00")), quantity);
    }
}
