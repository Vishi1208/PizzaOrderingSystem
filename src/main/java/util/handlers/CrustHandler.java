package util.handlers;

import java.util.Arrays;
import java.util.List;

public class CrustHandler extends ValidationHandler {

    private final List<String> validCrusts = Arrays.asList("Thin", "Thick", "Stuffed");

    @Override
    public boolean handle(String input) {

        if (input == null || input.trim().isEmpty()) {
            return false;
        }


        boolean isValidCrust = validCrusts.stream()
                .anyMatch(validCrust -> validCrust.equalsIgnoreCase(input.trim()));


        return isValidCrust && handleNext(input);
    }
}
