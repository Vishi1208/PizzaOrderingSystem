package util.decorators;

import model.Pizza;

public abstract class PizzaDecorator extends Pizza {
    protected Pizza pizza;

    // Constructor
    public PizzaDecorator(Pizza pizza) {
        super(pizza.getCrust(), pizza.getSauce(), pizza.getTopping(), pizza.getCheese());
        this.pizza = pizza;
    }

    public abstract double calculatePrice(double basePrice);

    @Override
    public String getDescription() {
        return pizza.getDescription();
    }
}
