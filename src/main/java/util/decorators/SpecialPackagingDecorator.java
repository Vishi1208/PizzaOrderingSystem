package util.decorators;

import model.Pizza;

public class SpecialPackagingDecorator extends PizzaDecorator {
    private static final double SPECIAL_PACKAGING_COST = 2.0;

    public SpecialPackagingDecorator(Pizza pizza) {
        super(pizza);
    }

    @Override
    public double calculatePrice(double basePrice) {
        return basePrice + SPECIAL_PACKAGING_COST;
    }

    @Override
    public String getDescription() {
        return pizza.getDescription() + ", Special Packaging";
    }
}
