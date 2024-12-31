// File: gui/SeasonalSpecialsGUI.java
package gui;

import model.Order;
import model.Promotion;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SeasonalSpecialsGUI {
    private JFrame frame;
    private Order order;
    private JTextArea specialsArea;
    private static final String PROMOTION_FILE = "promotions.dat"; // File to load promotions

    public SeasonalSpecialsGUI(Order order) {
        this.order = order;

        initializeFrame();
        addHeader();
        addPromotionsArea();
        addDiscountPanel();
        startAutoRefresh();

        frame.setVisible(true);
    }


    private void initializeFrame() {
        frame = new JFrame("Seasonal Specials & Promotions");
        frame.setSize(700, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
    }

    private void addHeader() {
        JLabel titleLabel = new JLabel("Seasonal Specials & Promotions", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(255, 153, 51));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        try {
            URL iconURL = new URL("https://img.icons8.com/color/48/discount.png");
            ImageIcon icon = new ImageIcon(iconURL);
            titleLabel.setIcon(icon);
        } catch (Exception e) {
            e.printStackTrace();
        }

        frame.add(titleLabel, BorderLayout.NORTH);
    }


    private void addPromotionsArea() {
        specialsArea = new JTextArea();
        specialsArea.setEditable(false);
        specialsArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        specialsArea.setText(loadPromotionsText());
        specialsArea.setBackground(new Color(255, 250, 240));
        specialsArea.setBorder(BorderFactory.createTitledBorder("Current Promotions"));
        specialsArea.setLineWrap(true);
        specialsArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(specialsArea);
        frame.add(scrollPane, BorderLayout.CENTER);
    }

    private void addDiscountPanel() {
        JPanel discountPanel = new JPanel(new GridBagLayout());
        discountPanel.setBorder(BorderFactory.createTitledBorder("Apply Discount"));
        discountPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel discountLabel = new JLabel("Enter Discount Code:");
        discountLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        discountPanel.add(discountLabel, gbc);

        JTextField discountField = new JTextField(15);
        discountField.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 1;
        discountPanel.add(discountField, gbc);

        JButton applyButton = new JButton("Apply Discount");
        try {
            URL applyIconURL = new URL("https://img.icons8.com/color/24/checkmark.png");
            applyButton.setIcon(new ImageIcon(applyIconURL));
        } catch (Exception e) {
            e.printStackTrace();
        }
        gbc.gridx = 2;
        discountPanel.add(applyButton, gbc);

        applyButton.addActionListener(e -> {
            String discountCode = discountField.getText().trim();
            if (!discountCode.isEmpty()) {
                applyDiscountCode(discountCode);
                discountField.setText("");
            } else {
                JOptionPane.showMessageDialog(frame, "Please enter a valid discount code!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.add(discountPanel, BorderLayout.SOUTH);
    }

    private void applyDiscountCode(String discountCode) {
        boolean applied = false;

        for (Promotion promotion : loadPromotions()) {
            if (discountCode.equalsIgnoreCase(promotion.getCode().trim())) {
                applyDiscount(promotion.getDiscount(), promotion.getCode());
                applied = true;
                break;
            }
        }

        if (!applied) {
            JOptionPane.showMessageDialog(frame, "Invalid Discount Code!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void applyDiscount(double discount, String code) {
        try {
            double discountAmount = order.applyDiscount(discount, code);
            JOptionPane.showMessageDialog(
                    frame,
                    "Discount Applied! You saved $" + String.format("%.2f", discountAmount),
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private String loadPromotionsText() {
        List<Promotion> promotions = loadPromotions();

        if (promotions.isEmpty()) {
            return "No seasonal specials available at the moment.";
        }

        StringBuilder builder = new StringBuilder();
        for (Promotion promotion : promotions) {
            builder.append(promotion).append("\n");
        }
        return builder.toString();
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

    private void startAutoRefresh() {
        Timer timer = new Timer(10000, e -> specialsArea.setText(loadPromotionsText())); // Refresh every 10 seconds
        timer.start();
    }
}
