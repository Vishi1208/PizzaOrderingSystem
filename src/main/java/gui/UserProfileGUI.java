package gui;

import model.Order;
import model.Pizza;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class UserProfileGUI {
    private JFrame frame;
    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextArea favoritePizzasArea;
    private JLabel loyaltyPointsLabel;
    private User user;
    private Order order;

    public UserProfileGUI(User user, Order order) {
        this.user = user;
        this.order = order;

        frame = new JFrame("User Profile");
        frame.setSize(800, 650);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(245, 245, 245)); // Light Gray background


        addHeader();
        addUserInfoSection();
        addFavoritePizzasSection();
        addActionButtons();

        frame.setVisible(true);
    }

    private void addHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(70, 130, 180)); // Steel Blue background
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Welcome, " + user.getName() + "!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);

        loyaltyPointsLabel = new JLabel("Loyalty Points: " + user.getLoyaltyPoints(), SwingConstants.RIGHT);
        loyaltyPointsLabel.setFont(new Font("Arial", Font.BOLD, 18));
        loyaltyPointsLabel.setForeground(Color.WHITE);

        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(loyaltyPointsLabel, BorderLayout.EAST);

        frame.add(headerPanel, BorderLayout.NORTH);
    }

    private void addUserInfoSection() {
        JPanel userInfoPanel = new JPanel(new GridLayout(4, 2, 15, 15));
        userInfoPanel.setBorder(BorderFactory.createTitledBorder("User Information"));
        userInfoPanel.setBackground(new Color(250, 250, 250)); // White background

        userInfoPanel.add(new JLabel("Name:"));
        nameField = new JTextField(user.getName());
        nameField.setFont(new Font("Arial", Font.PLAIN, 16));
        userInfoPanel.add(nameField);

        userInfoPanel.add(new JLabel("Email:"));
        emailField = new JTextField(user.getEmail());
        emailField.setFont(new Font("Arial", Font.PLAIN, 16));
        userInfoPanel.add(emailField);

        userInfoPanel.add(new JLabel("Phone:"));
        phoneField = new JTextField(user.getPhone());
        phoneField.setFont(new Font("Arial", Font.PLAIN, 16));
        userInfoPanel.add(phoneField);

        JButton updateButton = new JButton("Update Profile");
        updateButton.setBackground(new Color(102, 205, 170)); // Medium Aquamarine
        updateButton.setFont(new Font("Arial", Font.BOLD, 14));
        updateButton.addActionListener(e -> updateUserProfile());
        userInfoPanel.add(new JLabel()); // Empty placeholder
        userInfoPanel.add(updateButton);

        frame.add(userInfoPanel, BorderLayout.WEST);
    }

    private void addFavoritePizzasSection() {
        favoritePizzasArea = new JTextArea();
        favoritePizzasArea.setEditable(false);
        favoritePizzasArea.setFont(new Font("Arial", Font.PLAIN, 14));
        favoritePizzasArea.setBackground(new Color(250, 250, 250));
        updateFavoritePizzasArea();

        JScrollPane scrollPane = new JScrollPane(favoritePizzasArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Favorite Pizzas"));
        scrollPane.setPreferredSize(new Dimension(400, 350));

        frame.add(scrollPane, BorderLayout.CENTER);
    }

    private void addActionButtons() {
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 20, 20));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton addFavoriteButton = new JButton("Add Favorite");
        JButton removeFavoriteButton = new JButton("Remove Favorite");
        JButton orderFavoritesButton = new JButton("Order Favorites");

        // Styling buttons
        addFavoriteButton.setBackground(new Color(173, 216, 230)); // Light Blue
        addFavoriteButton.setFont(new Font("Arial", Font.BOLD, 14));

        removeFavoriteButton.setBackground(new Color(255, 160, 122)); // Light Coral
        removeFavoriteButton.setFont(new Font("Arial", Font.BOLD, 14));

        orderFavoritesButton.setBackground(new Color(144, 238, 144)); // Light Green
        orderFavoritesButton.setFont(new Font("Arial", Font.BOLD, 14));

        // Add button actions
        addFavoriteButton.addActionListener(e -> addFavoritePizza());
        removeFavoriteButton.addActionListener(e -> removeFavoritePizza());
        orderFavoritesButton.addActionListener(e -> orderFavoritePizzas());

        buttonPanel.add(addFavoriteButton);
        buttonPanel.add(removeFavoriteButton);
        buttonPanel.add(orderFavoritesButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void updateUserProfile() {
        String newName = nameField.getText().trim();
        String newEmail = emailField.getText().trim();
        String newPhone = phoneField.getText().trim();

        if (newName.isEmpty() || newEmail.isEmpty() || newPhone.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        user.setName(newName);
        user.setEmail(newEmail);
        user.setPhone(newPhone);

        JOptionPane.showMessageDialog(frame, "User profile updated!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void addFavoritePizza() {
        String crust = JOptionPane.showInputDialog(frame, "Enter Crust Type (e.g., Thin, Stuffed):");
        String sauce = JOptionPane.showInputDialog(frame, "Enter Sauce Type (e.g., Tomato, BBQ):");
        String topping = JOptionPane.showInputDialog(frame, "Enter Topping (e.g., Cheese, Pepperoni):");
        String cheese = JOptionPane.showInputDialog(frame, "Enter Cheese Type (e.g., Mozzarella, Parmesan):");

        if (crust != null && sauce != null && topping != null && cheese != null) {
            Pizza newFavorite = new Pizza(crust, sauce, topping, cheese);
            user.addFavoritePizza(newFavorite);
            JOptionPane.showMessageDialog(frame, "Pizza added to favorites!");
            updateFavoritePizzasArea();
        }
    }

    private void removeFavoritePizza() {
        List<Pizza> favorites = user.getFavoritePizzas();
        if (favorites.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No favorite pizzas to remove!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String input = JOptionPane.showInputDialog(frame, "Enter the number of the pizza to remove:");
        try {
            int index = Integer.parseInt(input) - 1;
            if (index >= 0 && index < favorites.size()) {
                Pizza pizza = favorites.get(index);
                user.removeFavoritePizza(pizza);
                JOptionPane.showMessageDialog(frame, "Pizza removed from favorites!");
                updateFavoritePizzasArea();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid number!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void orderFavoritePizzas() {
        List<Pizza> favorites = user.getFavoritePizzas();
        if (favorites.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No favorite pizzas to order!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String input = JOptionPane.showInputDialog(frame, "Enter the numbers of the pizzas to order (comma-separated):");
        try {
            String[] indices = input.split(",");
            StringBuilder orderSummary = new StringBuilder("Order Summary:\n");

            for (String indexStr : indices) {
                int index = Integer.parseInt(indexStr.trim()) - 1;
                if (index >= 0 && index < favorites.size()) {
                    Pizza pizza = favorites.get(index);
                    order.addPizza(pizza);
                    orderSummary.append("- ").append(pizza.getDescription()).append("\n");
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid number: " + (index + 1), "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            int confirm = JOptionPane.showConfirmDialog(
                    frame,
                    orderSummary + "\n\nDo you want to place this order?",
                    "Order Confirmation",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                order.updateState(util.OrderState.PLACED);
                JOptionPane.showMessageDialog(frame, "Your order has been placed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid input! Please enter valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateFavoritePizzasArea() {
        favoritePizzasArea.setText(user.displayFavorites());
        loyaltyPointsLabel.setText("Loyalty Points: " + user.getLoyaltyPoints()); // Update loyalty points display
    }
}
