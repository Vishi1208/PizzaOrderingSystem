package gui;

import model.Order;
import model.OrderManager;
import util.OrderState;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class AdminOrderManagementGUI {
    private final JFrame frame;
    private final JComboBox<String> orderSelector;
    private final JLabel orderStatusLabel;
    private final DefaultComboBoxModel<String> comboBoxModel;

    public AdminOrderManagementGUI() {
        frame = new JFrame("Admin Order Management");
        frame.setSize(600, 500);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Admin: Manage Orders", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        frame.add(titleLabel, BorderLayout.NORTH);

        JPanel orderPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        comboBoxModel = new DefaultComboBoxModel<>();
        orderSelector = new JComboBox<>(comboBoxModel);
        orderSelector.addActionListener(e -> updateOrderStatusLabel());
        orderPanel.add(new JLabel("Order Number:"));
        orderPanel.add(orderSelector);
        frame.add(orderPanel, BorderLayout.NORTH);

        orderStatusLabel = new JLabel("Order Status: No orders found.", SwingConstants.CENTER);
        orderStatusLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        frame.add(orderStatusLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        JButton preparingButton = new JButton("Set to Preparing");
        JButton outForDeliveryButton = new JButton("Set to Out for Delivery");
        JButton completedButton = new JButton("Set to Completed");

        preparingButton.addActionListener(e -> updateOrderState(OrderState.PREPARING));
        outForDeliveryButton.addActionListener(e -> updateOrderState(OrderState.OUT_FOR_DELIVERY));
        completedButton.addActionListener(e -> updateOrderState(OrderState.COMPLETED));

        buttonPanel.add(preparingButton);
        buttonPanel.add(outForDeliveryButton);
        buttonPanel.add(completedButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);


        OrderManager.getInstance().addOrderObserver("AdminGUI", unused -> SwingUtilities.invokeLater(this::loadOrders));
        loadOrders();

        frame.setVisible(true);
    }

    private void loadOrders() {
        comboBoxModel.removeAllElements();
        Map<String, Order> allOrders = OrderManager.getInstance().getAllOrders();
        for (String orderId : allOrders.keySet()) {
            comboBoxModel.addElement(orderId);
        }
        updateOrderStatusLabel();
    }

    private void updateOrderStatusLabel() {
        Order selectedOrder = getSelectedOrder();
        if (selectedOrder != null) {
            orderStatusLabel.setText("Order Status: " + selectedOrder.getStateDescription());
        } else {
            orderStatusLabel.setText("Order Status: No order selected.");
        }
    }

    private void updateOrderState(OrderState newState) {
        Order selectedOrder = getSelectedOrder();
        if (selectedOrder != null) {
            selectedOrder.updateState(newState);
            OrderManager.getInstance().updateOrder(selectedOrder); // Notify observers about the change
        } else {
            JOptionPane.showMessageDialog(frame, "No order selected!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Order getSelectedOrder() {
        String selectedOrderNumber = (String) orderSelector.getSelectedItem();
        return selectedOrderNumber != null ? OrderManager.getInstance().getOrder(selectedOrderNumber) : null;
    }
}
