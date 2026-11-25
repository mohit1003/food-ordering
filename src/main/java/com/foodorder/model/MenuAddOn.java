package com.foodorder.model;

public enum MenuAddOn {
    CHEESE("Cheese") {
        @Override
        public PricedItem applyTo(PricedItem item) {
            return new CheeseDecorator(item);
        }
    },
    EXTRA_PATTY("Extra Patty") {
        @Override
        public PricedItem applyTo(PricedItem item) {
            return new ExtraPattyDecorator(item);
        }
    },
    PERIPERI("Peri Peri masala") {
        public PricedItem applyTo(PricedItem item) {
            return new ExtraPeriPeriDecorator(item);
        }
    };
    private final String label;

    MenuAddOn(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }

    public abstract PricedItem applyTo(PricedItem item);
}
