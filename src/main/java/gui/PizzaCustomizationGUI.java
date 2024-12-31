package gui;

import model.Order;
import model.OrderManager;
import model.Pizza;
import model.User;
import util.decorators.ExtraCheeseDecorator;
import util.decorators.SpecialPackagingDecorator;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class PizzaCustomizationGUI {
    private JFrame frame;
    private JTextField pizzaNameField;
    private JComboBox<String> crustComboBox;
    private JComboBox<String> sauceComboBox;
    private JComboBox<String> toppingComboBox;
    private JComboBox<String> cheeseComboBox;
    private JComboBox<String> sizeComboBox;
    private JCheckBox extraCheeseCheckBox;
    private JCheckBox specialPackagingCheckBox;
    private JSpinner quantitySpinner;
    private JLabel priceLabel;
    private Order order;
    private User user;

    public PizzaCustomizationGUI(User user) {
        this.user = user;

        this.order = OrderManager.getInstance().getOrderForUser(user);
        if (this.order == null) {
            this.order = OrderManager.getInstance().createNewOrderForUser(user);
        }

        initializeFrame();

        buildComponents();

        frame.setVisible(true);
    }

    private void initializeFrame() {
        frame = new JFrame("Pizza Customization");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
    }

    private void buildComponents() {
        addTitle();
        addCustomizationPanel();
        addButtonPanel();
    }

    private void addTitle() {
        JLabel titleLabel = new JLabel("Customize Your Pizza", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        try {
            URL iconURL = new URL("https://img.icons8.com/color/48/pizza.png");
            ImageIcon icon = new ImageIcon(iconURL);
            titleLabel.setIcon(icon);
        } catch (Exception e) {
            e.printStackTrace();
        }

        frame.add(titleLabel, BorderLayout.NORTH);
    }

    private void addCustomizationPanel() {
        JPanel customizationPanel = new JPanel(new GridLayout(10, 2, 10, 10));
        customizationPanel.setBorder(BorderFactory.createTitledBorder("Select Pizza Options"));

        // Pizza name
        pizzaNameField = new JTextField();
        customizationPanel.add(new JLabel("Custom Pizza Name:"));
        customizationPanel.add(pizzaNameField);

        // Pizza size
        sizeComboBox = new JComboBox<>(new String[]{"Small", "Medium", "Large"});
        sizeComboBox.addActionListener(e -> updatePrice());
        customizationPanel.add(new JLabel("Select Size:"));
        customizationPanel.add(sizeComboBox);

        // Crust options
        crustComboBox = new JComboBox<>(new String[]{"Thin", "Thick", "Stuffed"});
        crustComboBox.addActionListener(e -> updatePrice());
        customizationPanel.add(new JLabel("Select Crust:"));
        customizationPanel.add(crustComboBox);

        // Sauce options
        sauceComboBox = new JComboBox<>(new String[]{"Tomato", "Pesto", "BBQ"});
        customizationPanel.add(new JLabel("Select Sauce:"));
        customizationPanel.add(sauceComboBox);

        // Topping options
        toppingComboBox = new JComboBox<>(new String[]{"Cheese", "Pepperoni", "Mushrooms", "Olives"});
        toppingComboBox.addActionListener(e -> updatePrice());
        customizationPanel.add(new JLabel("Select Topping:"));
        customizationPanel.add(toppingComboBox);

        // Cheese options
        cheeseComboBox = new JComboBox<>(new String[]{"Mozzarella", "Cheddar", "Parmesan"});
        cheeseComboBox.addActionListener(e -> updatePrice());
        customizationPanel.add(new JLabel("Select Cheese:"));
        customizationPanel.add(cheeseComboBox);

        // Quantity
        quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        quantitySpinner.addChangeListener(e -> updatePrice());
        customizationPanel.add(new JLabel("Select Quantity:"));
        customizationPanel.add(quantitySpinner);

        // Extra features
        extraCheeseCheckBox = new JCheckBox("Add Extra Cheese");
        extraCheeseCheckBox.addActionListener(e -> updatePrice());
        customizationPanel.add(extraCheeseCheckBox);

        specialPackagingCheckBox = new JCheckBox("Add Special Packaging");
        specialPackagingCheckBox.addActionListener(e -> updatePrice());
        customizationPanel.add(specialPackagingCheckBox);

        // Total price
        priceLabel = new JLabel("Total Price: $0.00");
        priceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        customizationPanel.add(new JLabel());
        customizationPanel.add(priceLabel);

        frame.add(customizationPanel, BorderLayout.CENTER);
    }

    private void addButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton placeOrderButton = new JButton("Place Order");
        JButton addToFavoritesButton = new JButton("Add to Favorites");
        JButton cancelButton = new JButton("Cancel");

        placeOrderButton.addActionListener(e -> placeOrder());
        addToFavoritesButton.addActionListener(e -> savePizzaAsFavorite());
        cancelButton.addActionListener(e -> frame.dispose());

        buttonPanel.add(placeOrderButton);
        buttonPanel.add(addToFavoritesButton);
        buttonPanel.add(cancelButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void updatePrice() {
        double sizePrice = 0.0;
        String size = (String) sizeComboBox.getSelectedItem();
        if ("Medium".equals(size)) {
            sizePrice = 8.0;
        } else if ("Large".equals(size)) {
            sizePrice = 12.0;
        } else {
            sizePrice = 5.0;
        }

        double crustPrice = 0.0;
        String crust = (String) crustComboBox.getSelectedItem();
        if ("Stuffed".equals(crust)) {
            crustPrice = 2.0;
        } else if ("Thick".equals(crust)) {
            crustPrice = 1.5;
        }

        double toppingPrice = toppingComboBox.getSelectedItem().toString().equalsIgnoreCase("Pepperoni") ? 1.5 : 0.0;
        double cheesePrice = cheeseComboBox.getSelectedItem().toString().equalsIgnoreCase("Mozzarella") ? 2.0 : 0.0;

        double extraCheeseCost = extraCheeseCheckBox.isSelected() ? 1.5 : 0.0;
        double specialPackagingCost = specialPackagingCheckBox.isSelected() ? 2.0 : 0.0;

        int quantity = (int) quantitySpinner.getValue();

        double totalPrice = (sizePrice + crustPrice + toppingPrice + cheesePrice + extraCheeseCost + specialPackagingCost) * quantity;

        priceLabel.setText(String.format("Total Price: $%.2f", totalPrice));
    }

    private void placeOrder() {
        if (order.getOrderId() == null || order.getOrderId().isEmpty()) {
            order = OrderManager.getInstance().createNewOrderForUser(user);
        }
        addPizzaToOrder();
        OrderManager.getInstance().updateOrder(order);
        JOptionPane.showMessageDialog(frame, "Order placed! Order ID: " + order.getOrderId(), "Success", JOptionPane.INFORMATION_MESSAGE);
        new OrderReviewGUI(order, user);
        new OrderTrackingGUI(order);
        frame.dispose();
    }

    private void savePizzaAsFavorite() {
        String pizzaName = pizzaNameField.getText().trim();
        if (pizzaName.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please provide a name for your pizza!");
            return;
        }

        String crust = (String) crustComboBox.getSelectedItem();
        String sauce = (String) sauceComboBox.getSelectedItem();
        String topping = (String) toppingComboBox.getSelectedItem();
        String cheese = (String) cheeseComboBox.getSelectedItem();

        Pizza favoritePizza = new Pizza(crust, sauce, topping, cheese);
        favoritePizza.setName(pizzaName);
        user.addFavoritePizza(favoritePizza);

        JOptionPane.showMessageDialog(frame, "Pizza added to favorites!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void addPizzaToOrder() {
        String pizzaName = pizzaNameField.getText().trim();
        if (pizzaName.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please provide a name for your pizza!");
            return;
        }

        String crust = (String) crustComboBox.getSelectedItem();
        String sauce = (String) sauceComboBox.getSelectedItem();
        String topping = (String) toppingComboBox.getSelectedItem();
        String cheese = (String) cheeseComboBox.getSelectedItem();
        int quantity = (int) quantitySpinner.getValue();

        for (int i = 0; i < quantity; i++) {
            Pizza pizza = new Pizza(crust, sauce, topping, cheese);
            pizza.setName(pizzaName);

            if (extraCheeseCheckBox.isSelected()) {
                pizza = new ExtraCheeseDecorator(pizza);
            }
            if (specialPackagingCheckBox.isSelected()) {
                pizza = new SpecialPackagingDecorator(pizza);
            }

            order.addPizza(pizza);
        }
    }
}
