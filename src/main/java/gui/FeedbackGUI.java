package gui;

import model.Order;

import javax.swing.*;
import java.awt.*;

public class FeedbackGUI {
    private JFrame frame;
    private Order order;

    public FeedbackGUI(Order order) {
        // Validate the order
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null.");
        }
        this.order = order;

        frame = new JFrame("Feedback & Ratings");
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JLabel headerLabel = new JLabel("Provide Your Feedback", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setOpaque(true);
        headerLabel.setBackground(new Color(255, 153, 51));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        frame.add(headerLabel, BorderLayout.NORTH);

        // Feedback form
        JPanel feedbackPanel = new JPanel(new GridBagLayout());
        feedbackPanel.setBorder(BorderFactory.createTitledBorder("Your Feedback"));
        feedbackPanel.setBackground(new Color(250, 250, 250));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Rating selection
        gbc.gridx = 0;
        gbc.gridy = 0;
        feedbackPanel.add(new JLabel("Rating (1-5):"), gbc);

        JComboBox<Integer> ratingComboBox = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
        ratingComboBox.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 1;
        feedbackPanel.add(ratingComboBox, gbc);

        // Feedback text area
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        feedbackPanel.add(new JLabel("Your Feedback:"), gbc);

        JTextArea feedbackTextArea = new JTextArea(5, 30);
        feedbackTextArea.setLineWrap(true);
        feedbackTextArea.setWrapStyleWord(true);
        feedbackTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane feedbackScrollPane = new JScrollPane(feedbackTextArea);
        gbc.gridy = 2;
        feedbackPanel.add(feedbackScrollPane, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton submitButton = createButton("Submit Feedback", "icons/submit.png");
        JButton clearButton = createButton("Clear", "icons/clear.png");
        JButton cancelButton = createButton("Cancel", "icons/cancel.png");

        buttonPanel.add(submitButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(cancelButton);

        gbc.gridy = 3;
        feedbackPanel.add(buttonPanel, gbc);

        frame.add(feedbackPanel, BorderLayout.CENTER);

        // Feedback history
        JTextArea feedbackHistoryArea = new JTextArea();
        feedbackHistoryArea.setEditable(false);
        feedbackHistoryArea.setFont(new Font("Arial", Font.PLAIN, 14));
        feedbackHistoryArea.setText(displayFeedbackSummary());
        JScrollPane historyScrollPane = new JScrollPane(feedbackHistoryArea);
        historyScrollPane.setBorder(BorderFactory.createTitledBorder("Feedback History"));
        frame.add(historyScrollPane, BorderLayout.SOUTH);

        submitButton.addActionListener(e -> {
            try {
                int rating = (int) ratingComboBox.getSelectedItem();
                String feedback = feedbackTextArea.getText().trim();

                if (feedback.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Feedback cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                order.setRating(rating);
                order.setFeedback(feedback);

                feedbackHistoryArea.setText(displayFeedbackSummary());
                JOptionPane.showMessageDialog(frame, "Thank you for your feedback!", "Success", JOptionPane.INFORMATION_MESSAGE);

                feedbackTextArea.setText("");
                ratingComboBox.setSelectedIndex(0);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        clearButton.addActionListener(e -> {
            feedbackTextArea.setText("");
            ratingComboBox.setSelectedIndex(0);
        });

        cancelButton.addActionListener(e -> frame.dispose());

        frame.setVisible(true);
    }

    private JButton createButton(String text, String iconPath) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        try {
            ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(iconPath));
            button.setIcon(icon);
        } catch (Exception e) {
            System.err.println("Icon not found: " + iconPath);
        }
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return button;
    }

    private String displayFeedbackSummary() {
        if (order == null) {
            return "No feedback available.";
        }
        StringBuilder summary = new StringBuilder();
        summary.append("Rating: ").append(order.getRating() == 0 ? "Not Rated" : order.getRating()).append("\n");
        summary.append("Feedback: ").append(order.getFeedback().isEmpty() ? "No Feedback Provided" : order.getFeedback()).append("\n");
        return summary.toString();
    }
}
