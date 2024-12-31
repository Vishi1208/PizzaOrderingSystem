
package util.commands;

import model.Order;

public class ApplyPromotionCommand implements command {
    private Order order;
    private String promoCode;
    private double discount;

    public ApplyPromotionCommand(Order order, String promoCode, double discount) {
        this.order = order;
        this.promoCode = promoCode;
        this.discount = discount;
    }

    @Override
    public void execute() {
        try {
            double savedAmount = order.applyDiscount(discount, promoCode);
            System.out.println("Promotion applied: " + promoCode);
            System.out.println("Saved: $" + savedAmount);
        } catch (IllegalArgumentException e) {
            System.err.println("Failed to apply promotion: " + e.getMessage());
        }
    }
}
