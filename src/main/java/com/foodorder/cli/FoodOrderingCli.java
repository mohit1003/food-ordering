package com.foodorder.cli;

import com.foodorder.model.*;
import com.foodorder.service.OrderService;
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
                    case "4" -> printOrder(service.placeOrder(cart));
                    case "5" -> findOrder();
                    case "0" -> { return; }
                    default -> System.out.println("Choose an option from the menu.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void addItem() {
        System.out.print("Item ID: ");
        int id = Integer.parseInt(input.nextLine().trim());
        System.out.print("Quantity: ");
        int quantity = Integer.parseInt(input.nextLine().trim());
        cart.add(service.findMenuItem(id), quantity);
        System.out.println("Added to cart.");
    }

    private void findOrder() {
        System.out.print("Order UUID: ");
        service.findOrder(UUID.fromString(input.nextLine().trim()))
                .ifPresentOrElse(this::printOrder, () -> System.out.println("Order not found."));
    }

    private void printOrder(Order order) {
        System.out.println("Order: " + order.id());
        System.out.println("Status: " + order.status());
        printItems(order.items());
        System.out.printf("Total: INR %.2f%n", order.total());
    }

    private void printItems(java.util.List<CartItem> items) {
        if (items.isEmpty()) System.out.println("No items yet.");
        items.forEach(line -> System.out.printf("%s x%d — INR %.2f%n",
                line.item().name(), line.quantity(), line.subtotal()));
    }
}
