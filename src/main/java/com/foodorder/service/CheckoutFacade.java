package com.foodorder.service;

import com.foodorder.model.Cart;
import com.foodorder.model.Order;
import com.foodorder.payment.PaymentGateway;
import com.foodorder.payment.PaymentResult;
import com.foodorder.pricing.DiscountStrategy;

import java.math.BigDecimal;

public class CheckoutFacade {

    private final OrderService orderService;


    public CheckoutFacade(OrderService orderService) {
        this.orderService = orderService;
    }

    public CheckoutResult checkout(
            Cart cart,
            DiscountStrategy discountStrategy,
            PaymentGateway paymentGateway,
            String customerName,
            String contactPerson,
            String deliveryInstructions) {

        if(cart.isEmpty()) {
            throw new IllegalArgumentException("Your cart is empty");
        }

        BigDecimal subTotaal = cart.total();
        BigDecimal discount = discountStrategy.calculateDiscount(subTotaal);

        if(discount == null || discount.signum() < 0 || discount.compareTo(subTotaal) > 0) {
            throw new IllegalArgumentException("Discount strategy returned an invalid discount");
        }

        BigDecimal amountDue = subTotaal.subtract(discount);

        if(amountDue.signum()>0) {
            PaymentResult  payment= paymentGateway.charge(amountDue);

            if(payment == null) {
                throw new IllegalArgumentException("Payment Gateway returned no result");
            }

            if(!payment.approved()) {
                return new CheckoutResult(false,null, payment.message());
            }
        }

        Order order = orderService.placeOrder(cart,discountStrategy,customerName,contactPerson,deliveryInstructions);

        return new CheckoutResult(true, order, amountDue.signum() == 0 ? "No Payment Due":"Payment Approved");

    }

}
