package gui;

import model.Order;
import model.OrderManager;
import util.OrderState;
import util.Observer;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderTrackingGUI implements Observer {
    private JFrame frame;
    private JLabel statusLabel;
    private JTextArea trackingLog;
    private final String orderId;

    public OrderTrackingGUI(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null.");
        }

        this.orderId = order.getOrderId();
        order.addObserver(this);

        frame = new JFrame("Order Tracking");
        frame.setSize(600, 450);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Track Your Order", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        frame.add(titleLabel, BorderLayout.NORTH);

        JPanel statusPanel = new JPanel(new BorderLayout());
        statusLabel = new JLabel("Order Status: " + order.getStateDescription(), SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        statusPanel.add(statusLabel, BorderLayout.CENTER);
        frame.add(statusPanel, BorderLayout.CENTER);

        trackingLog = new JTextArea();
        trackingLog.setEditable(false);
        trackingLog.setFont(new Font("Monospaced", Font.PLAIN, 14));
        trackingLog.setBorder(BorderFactory.createTitledBorder("Order Tracking Log"));
        frame.add(new JScrollPane(trackingLog), BorderLayout.SOUTH);

        logTrackingUpdate(order.getStateDescription());

        frame.setVisible(true);
    }

    @Override
    public void update(OrderState state) {
        SwingUtilities.invokeLater(() -> {
            Order order = OrderManager.getInstance().getOrder(orderId);
            if (order != null) {
                String description = order.getStateDescription();
                statusLabel.setText("Order Status: " + description);
                logTrackingUpdate(description);

                if (state == OrderState.COMPLETED) {
                    JOptionPane.showMessageDialog(frame, "Your order has been delivered. Redirecting to Feedback.", "Order Delivered", JOptionPane.INFORMATION_MESSAGE);
                    new FeedbackGUI(order); // Open Feedback GUI
                    frame.dispose();
                }
            }
        });
    }

    private void logTrackingUpdate(String update) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        trackingLog.append(String.format("[%s] %s%n", timestamp, update));
    }
}
