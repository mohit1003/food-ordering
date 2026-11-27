package com.foodorder.cli;

import com.foodorder.service.OrderService;
import com.foodorder.payment.CardProcessorAdapter;
import com.foodorder.payment.MockCardProcessor;
import com.foodorder.payment.MockWalletClient;
import com.foodorder.payment.PaymentOption;
import com.foodorder.payment.WalletProviderAdapter;
import com.foodorder.service.CheckoutFacade;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class FoodOrderingCliTest {
    @Test
    void addItemFlowOffersDecoratorsAndDisplaysCustomizedDiscountedOrder() {
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;
        var output = new ByteArrayOutputStream();
        var input = new ByteArrayInputStream(("2\n1\n2\ny\nn\n3\n4\n1\n10\n"
                + "Asha\n555-0100\nLeave at reception\n1\n0\n")
                .getBytes(StandardCharsets.UTF_8));

        try {
            System.setIn(input);
            System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));

            newCli().run();

            String displayed = output.toString(StandardCharsets.UTF_8);
            assertTrue(displayed.contains("Burger + Cheese x2 — INR 280.00"));
            assertTrue(displayed.contains("Subtotal: INR 280.00"));
            assertTrue(displayed.contains("Discount: INR 28.00"));
            assertTrue(displayed.contains("Total: INR 252.00"));
            assertTrue(displayed.contains("Customer: Asha"));
            assertTrue(displayed.contains("Contact: 555-0100"));
            assertTrue(displayed.contains("Delivery instructions: Leave at reception"));
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
    }

    @Test
    void lemonadeDoesNotOfferFoodAddOns() {
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;
        var output = new ByteArrayOutputStream();
        var input = new ByteArrayInputStream("2\n5\n1\n3\n4\n0\n\n\n\n1\n0\n"
                .getBytes(StandardCharsets.UTF_8));

        try {
            System.setIn(input);
            System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));

            newCli().run();

            String displayed = output.toString(StandardCharsets.UTF_8);
            assertTrue(displayed.contains("Lemonade x1 — INR 50.00"));
            assertTrue(!displayed.contains("Add Cheese?"));
            assertTrue(!displayed.contains("Add Extra Patty?"));
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
    }

    @Test
    void declinedPaymentIsShownAndCartRemainsAvailable() {
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;
        var output = new ByteArrayOutputStream();
        var input = new ByteArrayInputStream("2\n1\n1\nn\nn\n4\n0\n\n\n\n1\n3\n0\n"
                .getBytes(StandardCharsets.UTF_8));

        try {
            System.setIn(input);
            System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));

            newCli(decliningPaymentOptions()).run();

            String displayed = output.toString(StandardCharsets.UTF_8);
            assertTrue(displayed.contains("Checkout failed: Card payment declined"));
            assertTrue(displayed.contains("Burger x1 — INR 120.00"));
            assertFalse(displayed.contains("Order: "));
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
    }

    private static List<PaymentOption> approvedPaymentOptions() {
        return List.of(
                new PaymentOption("Card", new CardProcessorAdapter(new MockCardProcessor(true))),
                new PaymentOption("Wallet", new WalletProviderAdapter(new MockWalletClient(true)))
        );
    }

    private static List<PaymentOption> decliningPaymentOptions() {
        return List.of(
                new PaymentOption("Card", new CardProcessorAdapter(new MockCardProcessor(false))),
                new PaymentOption("Wallet", new WalletProviderAdapter(new MockWalletClient(true)))
        );
    }

    private static FoodOrderingCli newCli() {
        return newCli(approvedPaymentOptions());
    }

    private static FoodOrderingCli newCli(List<PaymentOption> paymentOptions) {
        var orderService = new OrderService();
        return new FoodOrderingCli(orderService, new CheckoutFacade(orderService), paymentOptions);
    }
}
