package util.handlers;

import java.util.Arrays;
import java.util.List;

public class SauceHandler extends ValidationHandler {
    private final List<String> validSauces = Arrays.asList("Tomato", "Pesto", "BBQ");

    @Override
    public boolean handle(String input) {
        // Validate the input
        if (input == null || input.trim().isEmpty()) {
            return false;
        }


        for (String validSauce : validSauces) {
            if (validSauce.equalsIgnoreCase(input.trim())) {

                return handleNext(input);
            }
        }

        return false;
    }
}
