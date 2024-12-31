package gui;

import model.Order;
import util.DeliveryEstimator;

import javax.swing.*;
import java.awt.*;

public class PickupDeliveryGUI {
    private final JFrame frame;
    private final Order order;

    public PickupDeliveryGUI(Order order) {
        this.order = order;

        frame = new JFrame("Pickup or Delivery");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Choose Order Type", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        frame.add(titleLabel, BorderLayout.NORTH);

        JPanel orderTypePanel = new JPanel(new GridLayout(1, 2));
        JRadioButton pickupButton = new JRadioButton("Pickup");
        JRadioButton deliveryButton = new JRadioButton("Delivery");
        ButtonGroup orderTypeGroup = new ButtonGroup();
        orderTypeGroup.add(pickupButton);
        orderTypeGroup.add(deliveryButton);
        pickupButton.setSelected(true);

        orderTypePanel.add(pickupButton);
        orderTypePanel.add(deliveryButton);

        JPanel addressPanel = new JPanel(new BorderLayout(10, 10));
        JLabel addressLabel = new JLabel("Enter Delivery Address:");
        JTextField addressField = new JTextField();
        addressPanel.add(addressLabel, BorderLayout.NORTH);
        addressPanel.add(addressField, BorderLayout.CENTER);
        addressPanel.setVisible(false);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton estimateButton = new JButton("Get Delivery Estimate");
        JButton confirmButton = new JButton("Confirm");
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(estimateButton);
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);

        deliveryButton.addActionListener(e -> addressPanel.setVisible(true));
        pickupButton.addActionListener(e -> addressPanel.setVisible(false));

        estimateButton.addActionListener(e -> {
            if (deliveryButton.isSelected()) {
                String address = addressField.getText().trim();
                if (!address.isEmpty()) {
                    // Get coordinates from the address
                    double[] coordinates = DeliveryEstimator.getCoordinates(address);
                    if (coordinates[0] != 0.0 && coordinates[1] != 0.0) {
                        // Get delivery estimate
                        String estimate = DeliveryEstimator.getDeliveryEstimate(coordinates);
                        JOptionPane.showMessageDialog(frame, "Delivery Estimate: " + estimate, "Estimate", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Unable to fetch coordinates for the address.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Please enter a valid delivery address!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Delivery option is not selected.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        confirmButton.addActionListener(e -> {
            if (deliveryButton.isSelected()) {
                String address = addressField.getText().trim();
                if (!address.isEmpty()) {
                    order.setOrderType("Delivery");
                    order.setDeliveryAddress(address);
                    JOptionPane.showMessageDialog(frame, "Delivery confirmed. Address: " + address, "Confirmation", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(frame, "Please enter a delivery address!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                order.setOrderType("Pickup");
                JOptionPane.showMessageDialog(frame, "Pickup confirmed.", "Confirmation", JOptionPane.INFORMATION_MESSAGE);
            }
            frame.dispose();
        });

        cancelButton.addActionListener(e -> frame.dispose());

        frame.add(orderTypePanel, BorderLayout.NORTH);
        frame.add(addressPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }
}
