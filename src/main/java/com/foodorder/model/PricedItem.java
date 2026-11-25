package com.foodorder.model;

import java.math.BigDecimal;

public interface PricedItem {
    String name();
    BigDecimal price();
}
