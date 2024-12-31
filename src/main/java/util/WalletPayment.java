package util;

public class WalletPayment implements PaymentStrategy {
    private String walletId;

    public WalletPayment(String walletId) {
        this.walletId = walletId;
    }

    @Override
    public boolean pay(double amount) {
        // Simulate payment processing
        System.out.println("Processing wallet payment...");
        System.out.println("Wallet ID: " + walletId);
        System.out.println("Amount: $" + amount);
        return true;
    }
}
