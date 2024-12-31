package util.handlers;

import java.util.Arrays;
import java.util.List;

public class CheeseHandler extends ValidationHandler {

    private final List<String> validCheeses = Arrays.asList("Mozzarella", "Cheddar", "Parmesan");

    @Override
    public boolean handle(String input) {

        if (input == null || input.trim().isEmpty()) {
            return false;
        }


        for (String validCheese : validCheeses) {
            if (validCheese.equalsIgnoreCase(input.trim())) {

                return handleNext(input);
            }
        }

        return false;
    }
}
