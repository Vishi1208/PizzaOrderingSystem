package util.decorators;

import model.Pizza;

public class ExtraCheeseDecorator extends PizzaDecorator {
    private static final double EXTRA_CHEESE_COST = 1.5;

    public ExtraCheeseDecorator(Pizza pizza) {
        super(pizza);
    }

    @Override
    public double calculatePrice(double basePrice) {
        return basePrice + EXTRA_CHEESE_COST; // Add the cost of extra cheese
    }

    @Override
    public String getDescription() {
        return pizza.getDescription() + ", Extra Cheese";
    }
}
