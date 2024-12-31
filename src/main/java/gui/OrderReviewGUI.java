package gui;

import model.Order;
import model.Pizza;
import model.User;
import util.commands.CommandInvoker;
import util.commands.PlaceOrderCommand;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

public class OrderReviewGUI {
    private final JFrame frame;
    private final JLabel totalPriceLabel;

    public OrderReviewGUI(Order order, User user) {
        if (user == null) {
            throw new IllegalArgumentException("User object cannot be null");
        }

        frame = new JFrame("Order Review");
        frame.setSize(900, 650);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Order Review", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(255, 102, 51));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        frame.add(titleLabel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(245, 245, 245));

        DefaultListModel<String> pizzaListModel = new DefaultListModel<>();
        for (Pizza pizza : order.getPizzas()) {
            pizzaListModel.addElement(pizza.getDescription());
        }
        JList<String> pizzaList = new JList<>(pizzaListModel);
        pizzaList.setFont(new Font("Arial", Font.PLAIN, 16));
        pizzaList.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pizzaList.setBackground(Color.WHITE);
        pizzaList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(pizzaList);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Your Pizza List",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 16)
        ));
        scrollPane.setPreferredSize(new Dimension(400, 300));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel orderDetailsPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        orderDetailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        orderDetailsPanel.setBackground(new Color(245, 245, 245));

        totalPriceLabel = new JLabel("Total Price: $" + String.format("%.2f", order.getTotalPrice()), SwingConstants.RIGHT);
        totalPriceLabel.setFont(new Font("Arial", Font.BOLD, 18));
        totalPriceLabel.setForeground(new Color(50, 50, 50));

        JLabel loyaltyPointsLabel = new JLabel("Loyalty Points Earned: " + order.getLoyaltyPointsEarned(), SwingConstants.RIGHT);
        loyaltyPointsLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        loyaltyPointsLabel.setForeground(new Color(100, 100, 100));

        orderDetailsPanel.add(totalPriceLabel);
        orderDetailsPanel.add(loyaltyPointsLabel);
        mainPanel.add(orderDetailsPanel, BorderLayout.SOUTH);

        frame.add(mainPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        buttonPanel.setBackground(new Color(245, 245, 245));

        JButton editButton = createStyledButton("Edit Pizza", "icons/edit.png", new Color(102, 204, 255));
        JButton removeButton = createStyledButton("Remove Pizza", "icons/remove.png", new Color(255, 153, 153));
        JButton confirmButton = createStyledButton("Confirm Order", "icons/confirm.png", new Color(153, 255, 153));
        JButton cancelButton = createStyledButton("Cancel", "icons/cancel.png", new Color(204, 204, 204));

        buttonPanel.add(editButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Button actions
        removeButton.addActionListener(e -> {
            int selectedIndex = pizzaList.getSelectedIndex();
            if (selectedIndex != -1) {
                List<Pizza> pizzas = order.getPizzas();
                if (selectedIndex < pizzas.size()) {
                    pizzas.remove(selectedIndex);
                    pizzaListModel.remove(selectedIndex);
                    order.calculateTotal(); // Recalculate the total price
                    updateTotalPriceLabel(order.getTotalPrice());
                    JOptionPane.showMessageDialog(frame, "Pizza removed successfully!");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a pizza to remove!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        confirmButton.addActionListener(e -> {
            if (order.getPizzas().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No pizzas in the order! Add pizzas before confirming.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            CommandInvoker invoker = new CommandInvoker();
            invoker.addCommand(new PlaceOrderCommand(order));
            invoker.executeCommands();
            frame.dispose();


            new PaymentGUI(user, order.getTotalPrice());
        });

        cancelButton.addActionListener(e -> {
            frame.dispose();
        });

        editButton.addActionListener(e -> {
            int selectedIndex = pizzaList.getSelectedIndex();
            if (selectedIndex != -1) {
                JOptionPane.showMessageDialog(frame, "Pizza editing functionality coming soon!", "Info", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a pizza to edit!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.setVisible(true);
    }

    private JButton createStyledButton(String text, String iconPath, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setFocusPainted(false);
        button.setBackground(backgroundColor);
        button.setForeground(Color.BLACK);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        try {
            ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(iconPath));
            button.setIcon(icon);
        } catch (Exception e) {
            System.err.println("Icon not found: " + iconPath);
        }
        return button;
    }

    private void updateTotalPriceLabel(double totalPrice) {
        totalPriceLabel.setText("Total Price: $" + String.format("%.2f", totalPrice));
    }
}
