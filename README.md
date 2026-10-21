# Food Ordering Simulator

A Java 21 + Gradle CLI starter for learning design patterns. The baseline intentionally uses simple classes; you implement the patterns through the exercises below.

## Run

Install JDK 21 and set JAVA_HOME to it. No database, web server, or real payment account is needed.

```sh
./gradlew run
./gradlew test
```

On Windows use `gradlew.bat run` and `gradlew.bat test`.

Start with menu option 1, add a burger (ID 1) with quantity 2, then view your cart and place the order. The total should be INR 240.00. Copy the printed UUID to look it up using option 5.

All data is in memory and resets on exit. Prices use BigDecimal. Repeated additions create separate cart lines. Checkout saves an immutable snapshot and clears the cart. Payment and status changes are future exercises.

## Learning roadmap

Implement one requirement at a time. Before refactoring, describe the limitation in the current code. Afterward, explain which classes can now change independently.

1. **Strategy:** Support flat and percentage discounts through a DiscountStrategy interface. Test rounding, zero discount, and discounts larger than the total.
2. **Decorator:** Add cheese and extra toppings, with composable prices. Consider an interface that both base items and decorated items implement.
3. **Builder:** Assemble orders with optional delivery instructions and contact details; validate required fields at build time.
4. **Adapter:** Give two mock payment providers different APIs, then adapt both to one PaymentGateway. Never use real credentials.
5. **Facade:** Introduce a CheckoutFacade coordinating validation, pricing, mock payment, and order creation. A failed payment should preserve the cart.
6. **State:** Add preparing, dispatched, delivered, and cancelled states. Reject invalid transitions, such as delivered to preparing.
7. **Observer:** Subscribe customer and restaurant listeners to order status changes without coupling Order to their implementations.
8. **Command:** Implement add/remove cart commands with undo. Verify that undo restores quantities correctly.
9. **Proxy:** Restrict refund operations to an admin using a proxy around the refund service. This is an access-control simulation.
10. **Factory Method:** Add restaurant subclasses whose ordering workflow calls an overridable createMenuItem method. A switch returning an item alone is a simple factory, not this exercise.
11. **Abstract Factory:** Provide vegetarian and non-vegetarian meal factories, each creating a related main, side, and drink.
12. **Singleton:** Explore an immutable shared configuration instance. Compare it with passing configuration through constructors; the application may not need a Singleton.

## Suggested future packages

Create payment/, pricing/, notification/, and service/ classes as exercises require them. Organize by responsibility rather than putting every pattern in its own package.

## Definition of done for each exercise

- The new behavior works through the CLI.
- Relevant edge cases have focused tests.
- You can identify the pattern participants and explain the tradeoff.
- Existing ordering still works.
