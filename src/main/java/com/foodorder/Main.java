package com.foodorder;

import com.foodorder.cli.FoodOrderingCli;
import com.foodorder.payment.CardProcessorAdapter;
import com.foodorder.payment.MockCardProcessor;
import com.foodorder.payment.MockWalletClient;
import com.foodorder.payment.PaymentOption;
import com.foodorder.payment.WalletProviderAdapter;
import com.foodorder.service.OrderService;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        List<PaymentOption> paymentOptions = List.of(
                new PaymentOption("Card", new CardProcessorAdapter(new MockCardProcessor(true))),
                new PaymentOption("Wallet", new WalletProviderAdapter(new MockWalletClient(true)))
        );
        new FoodOrderingCli(new OrderService(), paymentOptions).run();
    }
}
