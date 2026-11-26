package com.foodorder.payment;

import java.math.BigDecimal;

public class MockWalletClient {
    private final boolean paymentsEnabled;

    public MockWalletClient(boolean paymentsEnabled) {
        this.paymentsEnabled = paymentsEnabled;
    }

    public String sendMoney(String currencyCode, String amount) {
        if (!paymentsEnabled || !"INR".equals(currencyCode)) {
            return "DECLINED";
        }

        if (amount == null) {
            return "DECLINED";
        }
        try {
            return new BigDecimal(amount).signum() > 0 ? "SUCCESS" : "DECLINED";
        } catch (NumberFormatException e) {
            return "DECLINED";
        }
    }
}
