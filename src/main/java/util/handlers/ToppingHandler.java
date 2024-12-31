package util.handlers;

import java.util.Arrays;
import java.util.List;

public class ToppingHandler extends ValidationHandler {
    private final List<String> validToppings = Arrays.asList("Cheese", "Pepperoni", "Mushrooms", "Olives");

    @Override
    public boolean handle(String input) {
        // Validate the input
        if (input == null || input.trim().isEmpty()) {
            return false;
        }


        for (String validTopping : validToppings) {
            if (validTopping.equalsIgnoreCase(input.trim())) {
                return handleNext(input);
            }
        }


        return false;
    }
}
