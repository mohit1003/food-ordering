package com.foodorder.cli;

import com.foodorder.model.*;
import com.foodorder.pricing.DiscountStrategy;
import com.foodorder.pricing.FlatDiscountStrategy;
import com.foodorder.pricing.NoDiscountStrategy;
import com.foodorder.pricing.PercentageDiscountStrategy;
import com.foodorder.service.OrderService;
import java.math.BigDecimal;
import java.util.Scanner;
import java.util.UUID;

public class FoodOrderingCli {
    private final OrderService service;
    private final Cart cart = new Cart();
    private final Scanner input = new Scanner(System.in);

    public FoodOrderingCli(OrderService service) { this.service = service; }

    public void run() {
        System.out.println("Welcome to Food Ordering Simulator! Prices are in INR.");
        while (true) {
            System.out.println("\n1. View menu\n2. Add item\n3. View cart\n4. Place order\n5. Find order\n0. Exit");
            System.out.print("> ");
            if (!input.hasNextLine()) return;
            try {
                switch (input.nextLine().trim()) {
                    case "1" -> service.menu().forEach(item ->
                            System.out.printf("%d. %s — INR %.2f%n", item.id(), item.name(), item.price()));
                    case "2" -> addItem();
                    case "3" -> {
                        printItems(cart.items());
                        System.out.printf("Total: INR %.2f%n", cart.total());
                    }
                    case "4" -> printOrder(checkout());
                    case "5" -> findOrder();
                    case "0" -> { return; }
                    default -> System.out.println("Choose an option from the menu.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private DiscountStrategy selectDiscountStrategy() {
        System.out.println("Discount: 0. None  1. Percentage  2. Flat amount");
        System.out.print("> ");
        String choice = input.nextLine().trim();
        return switch (choice) {
            case "0" -> new NoDiscountStrategy();
            case "1" -> {
                System.out.print("Percentage (0-100): ");
                yield new PercentageDiscountStrategy(new BigDecimal(input.nextLine().trim()));
            }
            case "2" -> {
                System.out.print("Discount amount (INR): ");
                yield new FlatDiscountStrategy(new BigDecimal(input.nextLine().trim()));
            }
            default -> throw new IllegalArgumentException("Choose 0, 1, or 2 for the discount");
        };
    }

    private Order checkout() {
        if (cart.isEmpty()) {
            throw new IllegalArgumentException("Your cart is empty");
        }
        DiscountStrategy discountStrategy = selectDiscountStrategy();
        String customerName = readOptional("Customer name (optional): ");
        String contactPerson = readOptional("Contact person or phone (optional): ");
        String deliveryInstructions = readOptional("Delivery instructions (optional): ");
        return service.placeOrder(cart, discountStrategy,
                customerName, contactPerson, deliveryInstructions);
    }

    private String readOptional(String prompt) {
        System.out.print(prompt);
        String value = input.nextLine().trim();
        return value.isEmpty() ? null : value;
    }

    private void addItem() {
        System.out.print("Item ID: ");
        int id = Integer.parseInt(input.nextLine().trim());
        System.out.print("Quantity: ");
        int quantity = Integer.parseInt(input.nextLine().trim());

        MenuItem menuItem = service.findMenuItem(id);
        PricedItem item = menuItem;
        for (MenuAddOn addOn : menuItem.availableAddOns()) {
            if (confirm("Add " + addOn.label() + "? (y/n): ")) {
                item = addOn.applyTo(item);
            }
        }

        cart.add(item, quantity);
        System.out.println("Added to cart.");
    }

    private boolean confirm(String prompt) {
        System.out.print(prompt);
        return input.nextLine().trim().equalsIgnoreCase("y");
    }

    private void findOrder() {
        System.out.print("Order UUID: ");
        service.findOrder(UUID.fromString(input.nextLine().trim()))
                .ifPresentOrElse(this::printOrder, () -> System.out.println("Order not found."));
    }

    private void printOrder(Order order) {
        System.out.println("Order: " + order.id());
        System.out.println("Status: " + order.status());
        if (order.customerName() != null) {
            System.out.println("Customer: " + order.customerName());
        }
        if (order.contactPerson() != null) {
            System.out.println("Contact: " + order.contactPerson());
        }
        if (order.deliveryInstructions() != null) {
            System.out.println("Delivery instructions: " + order.deliveryInstructions());
        }
        printItems(order.items());
        System.out.printf("Subtotal: INR %.2f%n", order.subtotal());
        System.out.printf("Discount: INR %.2f%n", order.discount());
        System.out.printf("Total: INR %.2f%n", order.total());
    }

    private void printItems(java.util.List<CartItem> items) {
        if (items.isEmpty()) System.out.println("No items yet.");
        items.forEach(line -> System.out.printf("%s x%d — INR %.2f%n",
                line.item().name(), line.quantity(), line.subtotal()));
    }
}
