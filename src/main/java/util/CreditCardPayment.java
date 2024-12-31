package util;

public class CreditCardPayment implements PaymentStrategy {
    private String cardNumber;
    private String cardHolderName;

    public CreditCardPayment(String cardNumber, String cardHolderName) {
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
    }

    @Override
    public boolean pay(double amount) {
        // Simulate payment processing
        System.out.println("Processing credit card payment...");
        System.out.println("Card Holder: " + cardHolderName + ", Card Number: " + cardNumber);
        System.out.println("Amount: $" + amount);
        return true; // Assume payment is successful
    }
}
