package com.foodorder.service;

import com.foodorder.model.Order;

public record CheckoutResult(boolean completed, Order order, String message) {
    public CheckoutResult {
        if(completed && order == null) {
            throw new IllegalArgumentException("A completed checkout must include an order");
        }
        if(!completed && order != null) {
            throw new IllegalArgumentException("A declined checkout cannot include an order");
        }


    }
}
