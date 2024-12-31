package gui;

import model.Pizza;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FavoritesGUI {
    private JFrame frame;
    private User user;
    private DefaultListModel<Pizza> favoritesListModel;

    public FavoritesGUI(User user) {
        this.user = user;


        frame = new JFrame(user.getName() + "'s Favorites");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Your Favorite Pizza Combinations", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        frame.add(titleLabel, BorderLayout.NORTH);

        // Favorite pizzas list
        favoritesListModel = new DefaultListModel<>();
        for (Pizza pizza : user.getFavoritePizzas()) {
            favoritesListModel.addElement(pizza);
        }
        JList<Pizza> favoritesList = new JList<>(favoritesListModel);
        favoritesList.setCellRenderer(new PizzaListRenderer());
        JScrollPane scrollPane = new JScrollPane(favoritesList);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton reorderButton = new JButton("Reorder Selected");
        JButton removeButton = new JButton("Remove Selected");

        reorderButton.addActionListener(e -> {
            Pizza selectedPizza = favoritesList.getSelectedValue();
            if (selectedPizza != null) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Reordering " + selectedPizza.getDescription(),
                        "Reorder Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });

        removeButton.addActionListener(e -> {
            Pizza selectedPizza = favoritesList.getSelectedValue();
            if (selectedPizza != null) {
                user.removeFavoritePizza(selectedPizza);
                favoritesListModel.removeElement(selectedPizza);
            }
        });

        buttonPanel.add(reorderButton);
        buttonPanel.add(removeButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private static class PizzaListRenderer extends JLabel implements ListCellRenderer<Pizza> {
        @Override
        public Component getListCellRendererComponent(JList<? extends Pizza> list, Pizza value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            setText(value.getDescription());
            setOpaque(true);
            setBackground(isSelected ? Color.CYAN : Color.WHITE);
            setForeground(Color.BLACK);
            return this;
        }
    }
}
