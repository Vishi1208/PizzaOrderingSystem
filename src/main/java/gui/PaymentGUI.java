package gui;

import model.Promotion;
import model.User;
import util.CreditCardPayment;
import util.PaymentStrategy;
import util.WalletPayment;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PaymentGUI {
    private JFrame frame;
    private User user;
    private double orderAmount;
    private JLabel amountLabel;
    private JLabel loyaltyPointsLabel;
    private JTextArea promotionsArea;
    private static final String PROMOTION_FILE = "promotions.dat";
    private List<Promotion> promotions;

    public PaymentGUI(User user, double orderAmount) {
        this.user = user;
        this.orderAmount = orderAmount;


        promotions = loadPromotions();


        frame = new JFrame("Payment & Loyalty Program");
        frame.setSize(900, 750);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());


        JLabel titleLabel = new JLabel("Finalize Your Payment", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(33, 150, 243)); // Blue
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        try {
            URL iconURL = new URL("https://img.icons8.com/color/64/payment.png");
            titleLabel.setIcon(new ImageIcon(iconURL));
        } catch (Exception e) {
            e.printStackTrace();
        }
        frame.add(titleLabel, BorderLayout.NORTH);


        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);


        JPanel promotionsPanel = new JPanel(new BorderLayout());
        promotionsPanel.setBorder(BorderFactory.createTitledBorder("Available Promotions"));
        promotionsPanel.setBackground(Color.WHITE);

        promotionsArea = new JTextArea(loadPromotionsText());
        promotionsArea.setEditable(false);
        promotionsArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        promotionsArea.setBackground(new Color(245, 245, 245));
        promotionsArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        promotionsArea.setLineWrap(true);
        promotionsArea.setWrapStyleWord(true);

        JScrollPane promotionsScrollPane = new JScrollPane(promotionsArea);
        promotionsPanel.add(promotionsScrollPane, BorderLayout.CENTER);

        mainPanel.add(promotionsPanel, BorderLayout.WEST);

        JPanel orderDetailsPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        orderDetailsPanel.setBorder(BorderFactory.createTitledBorder("Order Details"));
        orderDetailsPanel.setBackground(Color.WHITE);

        amountLabel = new JLabel("Order Amount: $" + String.format("%.2f", orderAmount), SwingConstants.CENTER);
        amountLabel.setFont(new Font("Arial", Font.BOLD, 20));
        loyaltyPointsLabel = new JLabel("Current Loyalty Points: " + user.getLoyaltyPoints(), SwingConstants.CENTER);
        loyaltyPointsLabel.setFont(new Font("Arial", Font.BOLD, 16));

        orderDetailsPanel.add(amountLabel);
        orderDetailsPanel.add(loyaltyPointsLabel);
        mainPanel.add(orderDetailsPanel, BorderLayout.CENTER);

        JPanel discountPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        discountPanel.setBackground(Color.WHITE);
        JLabel discountLabel = new JLabel("Enter Promotion Code:");
        discountLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JTextField discountField = new JTextField(20);
        JButton applyDiscountButton = new JButton("Apply");

        applyDiscountButton.setFont(new Font("Arial", Font.BOLD, 14));
        applyDiscountButton.setBackground(new Color(76, 175, 80)); // Green
        applyDiscountButton.setForeground(Color.WHITE);

        applyDiscountButton.addActionListener(e -> {
            String discountCode = discountField.getText().trim();
            if (!discountCode.isEmpty()) {
                applyPromotionCode(discountCode);
                discountField.setText(""); // Clear the field after applying
            } else {
                JOptionPane.showMessageDialog(frame, "Please enter a valid promotion code!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        discountPanel.add(discountLabel);
        discountPanel.add(discountField);
        discountPanel.add(applyDiscountButton);

        mainPanel.add(discountPanel, BorderLayout.SOUTH);
        frame.add(mainPanel, BorderLayout.CENTER);

        JPanel paymentPanel = new JPanel(new GridLayout(3, 1, 20, 20));
        paymentPanel.setBorder(BorderFactory.createTitledBorder("Choose Payment Method"));
        paymentPanel.setBackground(Color.WHITE);

        JButton creditCardButton = createStyledButton("Pay with Credit Card", "https://img.icons8.com/color/48/credit-card.png", new Color(3, 169, 244)); // Blue
        JButton walletButton = createStyledButton("Pay with Wallet", "https://img.icons8.com/color/48/wallet.png", new Color(255, 193, 7)); // Amber
        JButton redeemPointsButton = createStyledButton("Redeem Loyalty Points", "https://img.icons8.com/color/48/loyalty.png", new Color(156, 39, 176)); // Purple

        creditCardButton.addActionListener(e -> processCreditCardPayment());
        walletButton.addActionListener(e -> processWalletPayment());
        redeemPointsButton.addActionListener(e -> redeemLoyaltyPoints());

        paymentPanel.add(creditCardButton);
        paymentPanel.add(walletButton);
        paymentPanel.add(redeemPointsButton);

        frame.add(paymentPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private JButton createStyledButton(String text, String iconURL, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        try {
            button.setIcon(new ImageIcon(new URL(iconURL)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return button;
    }

    private void processCreditCardPayment() {
        String cardNumber = JOptionPane.showInputDialog(frame, "Enter Card Number:");
        String cardHolder = JOptionPane.showInputDialog(frame, "Enter Card Holder Name:");
        if (cardNumber == null || cardNumber.isEmpty() || cardHolder == null || cardHolder.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Invalid card details!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        PaymentStrategy payment = new CreditCardPayment(cardNumber, cardHolder);
        completePayment(payment);
    }

    private void processWalletPayment() {
        String walletId = JOptionPane.showInputDialog(frame, "Enter Wallet ID:");
        if (walletId == null || walletId.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Invalid wallet ID!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        PaymentStrategy payment = new WalletPayment(walletId);
        completePayment(payment);
    }

    private void redeemLoyaltyPoints() {
        int loyaltyPoints = user.getLoyaltyPoints();
        if (loyaltyPoints == 0) {
            JOptionPane.showMessageDialog(frame, "You don't have enough loyalty points to redeem!", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            String pointsInput = JOptionPane.showInputDialog(frame, "Enter points to redeem (Max: " + loyaltyPoints + "):");
            if (pointsInput == null || pointsInput.isEmpty()) return;

            int pointsToRedeem = Integer.parseInt(pointsInput);
            if (pointsToRedeem > loyaltyPoints) {
                JOptionPane.showMessageDialog(frame, "You cannot redeem more points than you have!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double discount = pointsToRedeem * 0.1;
            if (discount > orderAmount) {
                pointsToRedeem = (int) (orderAmount / 0.1);
                discount = pointsToRedeem * 0.1;
            }

            orderAmount -= discount;
            user.subtractLoyaltyPoints(pointsToRedeem);

            // Update labels dynamically
            amountLabel.setText("Order Amount: $" + String.format("%.2f", orderAmount));
            loyaltyPointsLabel.setText("Current Loyalty Points: " + user.getLoyaltyPoints());

            JOptionPane.showMessageDialog(frame, "Redeemed " + pointsToRedeem + " points for a discount of $" + String.format("%.2f", discount), "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid input. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void completePayment(PaymentStrategy payment) {
        if (payment.pay(orderAmount)) {
            int loyaltyPointsEarned = (int) orderAmount / 10;
            user.addLoyaltyPoints(loyaltyPointsEarned);

            JOptionPane.showMessageDialog(frame, "Payment Successful! You earned " + loyaltyPointsEarned + " loyalty points.", "Success", JOptionPane.INFORMATION_MESSAGE);
            frame.dispose();
        } else {
            JOptionPane.showMessageDialog(frame, "Payment Failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private List<Promotion> loadPromotions() {
        List<Promotion> promotions = new ArrayList<>();
        File file = new File(PROMOTION_FILE);

        if (file.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                promotions = (ArrayList<Promotion>) in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return promotions;
    }

    private String loadPromotionsText() {
        if (promotions.isEmpty()) {
            return "No promotions available at the moment.";
        }

        StringBuilder builder = new StringBuilder();
        for (Promotion promotion : promotions) {
            builder.append(promotion.getCode()).append(" - ").append(promotion.getDescription())
                    .append(" (").append(promotion.getDiscount()).append("% off)\n");
        }
        return builder.toString();
    }

    private void applyPromotionCode(String code) {
        boolean applied = false;
        for (Promotion promo : promotions) {
            if (promo.getCode().equalsIgnoreCase(code)) {
                double discountAmount = orderAmount * (promo.getDiscount() / 100);
                orderAmount -= discountAmount;
                amountLabel.setText("Order Amount: $" + String.format("%.2f", orderAmount));
                JOptionPane.showMessageDialog(frame, "Promotion applied! You saved $" + String.format("%.2f", discountAmount), "Success", JOptionPane.INFORMATION_MESSAGE);
                applied = true;
                break;
            }
        }

        if (!applied) {
            JOptionPane.showMessageDialog(frame, "Invalid promotion code!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
