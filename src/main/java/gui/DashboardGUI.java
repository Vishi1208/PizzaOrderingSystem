package gui;

import model.Order;
import model.OrderManager;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class DashboardGUI {
    private final JFrame frame;
    private Order order;
    private final User user;

    public DashboardGUI(Order order, User user) {
        this.order = order;
        this.user = user;

        frame = new JFrame("Pizza Ordering System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(250, 250, 250));

        addHeader();
        addCentralPanel();
        addFooter();

        frame.setVisible(true);
    }

    private void addHeader() {
        // Header panel with logo and title
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(255, 94, 58));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel logoLabel = new JLabel();
        Image logoImage = getImage("https://img.icons8.com/fluency/96/pizza.png");
        if (logoImage != null) {
            logoLabel.setIcon(new ImageIcon(logoImage));
        } else {
            logoLabel.setText("Pizza Icon");
        }

        JLabel titleLabel = new JLabel("Welcome to Pizza World!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);

        headerPanel.add(logoLabel, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        frame.add(headerPanel, BorderLayout.NORTH);
    }

    private void addCentralPanel() {
        // Central panel with feature buttons
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 3, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panel.setBackground(new Color(250, 250, 250));

        // Buttons for each feature
        JButton pizzaCustomizationButton = createButton("Pizza Customization", "https://img.icons8.com/fluency/96/pizza.png");
        JButton orderTrackingButton = createButton("Order Tracking", "https://img.icons8.com/color/96/order-delivered.png");
        JButton userProfileButton = createButton("User Profile", "https://img.icons8.com/color/96/user.png");
        JButton paymentButton = createButton("Payment & Loyalty", "https://img.icons8.com/color/96/money-transfer.png");
        JButton feedbackButton = createButton("Feedback & Ratings", "https://img.icons8.com/color/96/feedback.png");
        JButton specialsButton = createButton("Seasonal Specials", "https://img.icons8.com/color/96/discount.png");
        JButton pickupDeliveryButton = createButton("Pickup/Delivery", "https://img.icons8.com/color/96/delivery.png");
        JButton adminLoginButton = createButton("Admin Login", "https://img.icons8.com/color/96/admin-settings-male.png");
        JButton exitButton = createButton("Exit", "https://img.icons8.com/color/96/exit.png");

        // Add action listeners to buttons
        pizzaCustomizationButton.addActionListener(e -> openPizzaCustomizationWindow());
        orderTrackingButton.addActionListener(e -> openOrderTrackingWindow());
        userProfileButton.addActionListener(e -> openUserProfileWindow());
        paymentButton.addActionListener(e -> openPaymentWindow());
        feedbackButton.addActionListener(e -> openFeedbackWindow());
        specialsButton.addActionListener(e -> openSeasonalSpecialsWindow());
        pickupDeliveryButton.addActionListener(e -> openPickupDeliveryWindow());
        adminLoginButton.addActionListener(e -> openAdminLoginWindow());
        exitButton.addActionListener(e -> System.exit(0));

        // Add buttons to the panel
        panel.add(pizzaCustomizationButton);
        panel.add(orderTrackingButton);
        panel.add(userProfileButton);
        panel.add(paymentButton);
        panel.add(feedbackButton);
        panel.add(specialsButton);
        panel.add(pickupDeliveryButton);
        panel.add(adminLoginButton);
        panel.add(exitButton);

        frame.add(panel, BorderLayout.CENTER);
    }

    private void addFooter() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(new Color(255, 94, 58));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel footerLabel = new JLabel("© 2024 Pizza World. All Rights Reserved.", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        footerLabel.setForeground(Color.WHITE);

        JPanel socialMediaPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        socialMediaPanel.setOpaque(false);
        JLabel facebookIcon = new JLabel(new ImageIcon(getImage("https://img.icons8.com/color/48/facebook.png")));
        JLabel twitterIcon = new JLabel(new ImageIcon(getImage("https://img.icons8.com/color/48/twitter.png")));
        JLabel instagramIcon = new JLabel(new ImageIcon(getImage("https://img.icons8.com/color/48/instagram-new.png")));
        socialMediaPanel.add(facebookIcon);
        socialMediaPanel.add(twitterIcon);
        socialMediaPanel.add(instagramIcon);

        footerPanel.add(footerLabel, BorderLayout.CENTER);
        footerPanel.add(socialMediaPanel, BorderLayout.SOUTH);

        frame.add(footerPanel, BorderLayout.SOUTH);
    }

    private JButton createButton(String text, String iconURL) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout());
        button.setFocusPainted(false);
        button.setBackground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        try {
            ImageIcon icon = new ImageIcon(getImage(iconURL));
            JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
            button.add(iconLabel, BorderLayout.CENTER);
        } catch (Exception e) {
            System.out.println("Icon not found: " + iconURL);
        }

        JLabel textLabel = new JLabel(text, SwingConstants.CENTER);
        textLabel.setFont(new Font("Arial", Font.BOLD, 14));
        textLabel.setForeground(new Color(60, 60, 60));
        button.add(textLabel, BorderLayout.SOUTH);

        return button;
    }

    private Image getImage(String url) {
        try {
            return new ImageIcon(new URL(url)).getImage();
        } catch (Exception e) {
            System.out.println("Error loading image: " + e.getMessage());
            return null;
        }
    }

    private void openPizzaCustomizationWindow() {
        if (order == null) {
            order = OrderManager.getInstance().createNewOrderForUser(user);
        }
        new PizzaCustomizationGUI(user);
    }

    private void openOrderTrackingWindow() {
        if (order == null) {
            JOptionPane.showMessageDialog(frame, "No active order to track!", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            new OrderTrackingGUI(order);
        }
    }

    private void openUserProfileWindow() {
        new UserProfileGUI(user, order);
    }

    private void openPaymentWindow() {
        if (order == null || order.getPizzas().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No pizzas in the order! Add pizzas before proceeding to payment.");
        } else {
            double orderAmount = order.calculateTotal();
            new PaymentGUI(user, orderAmount);
        }
    }

    private void openFeedbackWindow() {
        if (order == null) {
            JOptionPane.showMessageDialog(frame, "No order available for feedback!", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            new FeedbackGUI(order);
        }
    }

    private void openSeasonalSpecialsWindow() {
        new SeasonalSpecialsGUI(order);
    }

    private void openPickupDeliveryWindow() {
        new PickupDeliveryGUI(order);
    }

    private void openAdminLoginWindow() {
        String username = JOptionPane.showInputDialog(frame, "Enter Admin Username:");
        String password = JOptionPane.showInputDialog(frame, "Enter Admin Password:");

        if ("admin".equals(username) && "1234".equals(password)) {
            String[] options = {"Order Management", "Promotion Management"};
            int choice = JOptionPane.showOptionDialog(frame,
                    "Select Admin Feature:",
                    "Admin Dashboard",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]);

            if (choice == 0) {
                new AdminOrderManagementGUI();
            } else if (choice == 1) {
                new AdminPromotionGUI();
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid Admin Credentials", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
