package com.foodorder;

import com.foodorder.cli.FoodOrderingCli;
import com.foodorder.pricing.DiscountStrategy;
import com.foodorder.pricing.FlatDiscountStrategy;
import com.foodorder.pricing.NoDiscountStrategy;
import com.foodorder.pricing.PercentageDiscountStrategy;
import com.foodorder.service.OrderService;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {

        BigDecimal subtotal = new BigDecimal("500.00");

        DiscountStrategy noDiscount = new NoDiscountStrategy();

        DiscountStrategy percentage =
                new PercentageDiscountStrategy(new BigDecimal("10"));

        DiscountStrategy flat =
                new FlatDiscountStrategy(new BigDecimal("100"));

        System.out.println(noDiscount.calculateDiscount(subtotal));
        System.out.println(percentage.calculateDiscount(subtotal));
        System.out.println(flat.calculateDiscount(subtotal));


//        new FoodOrderingCli(new OrderService()).run();
    }
}
