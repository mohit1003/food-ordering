package com.foodorder;

import com.foodorder.cli.FoodOrderingCli;
import com.foodorder.service.OrderService;

public class Main {
    public static void main(String[] args) {
        new FoodOrderingCli(new OrderService()).run();
    }
}
