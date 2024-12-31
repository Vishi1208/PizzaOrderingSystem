package gui;

import model.Promotion;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class AdminPromotionGUI {
    private JFrame frame;
    private DefaultListModel<Promotion> promotionListModel;
    private ArrayList<Promotion> promotions;

    public AdminPromotionGUI() {

        loadPromotions();

        frame = new JFrame("Admin - Manage Promotions");
        frame.setSize(700, 500);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Manage Promotions", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        frame.add(titleLabel, BorderLayout.NORTH);

        // Promotions list
        promotionListModel = new DefaultListModel<>();
        for (Promotion promo : promotions) {
            promotionListModel.addElement(promo);
        }
        JList<Promotion> promotionList = new JList<>(promotionListModel);
        promotionList.setCellRenderer(new PromotionCellRenderer());
        JScrollPane scrollPane = new JScrollPane(promotionList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Promotions"));
        frame.add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Add Promotion");
        JButton editButton = new JButton("Edit Promotion");
        JButton deleteButton = new JButton("Delete Promotion");

        addButton.addActionListener(e -> addPromotion());
        editButton.addActionListener(e -> {
            Promotion selected = promotionList.getSelectedValue();
            if (selected != null) editPromotion(selected);
        });
        deleteButton.addActionListener(e -> {
            Promotion selected = promotionList.getSelectedValue();
            if (selected != null) deletePromotion(selected);
        });

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void addPromotion() {
        Promotion promotion = PromotionForm.showForm(null);
        if (promotion != null) {
            promotions.add(promotion);
            promotionListModel.addElement(promotion);
            savePromotions();
        }
    }

    private void editPromotion(Promotion promotion) {
        Promotion updated = PromotionForm.showForm(promotion);
        if (updated != null) {
            promotions.remove(promotion);
            promotions.add(updated);
            promotionListModel.clear();
            promotions.forEach(promotionListModel::addElement);
            savePromotions();
        }
    }

    private void deletePromotion(Promotion promotion) {
        promotions.remove(promotion);
        promotionListModel.removeElement(promotion);
        savePromotions();
    }

    private void loadPromotions() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("promotions.dat"))) {
            promotions = (ArrayList<Promotion>) in.readObject();
        } catch (Exception e) {
            promotions = new ArrayList<>();
        }
    }

    private void savePromotions() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("promotions.dat"))) {
            out.writeObject(promotions);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new AdminPromotionGUI();
    }

    private static class PromotionCellRenderer extends JLabel implements ListCellRenderer<Promotion> {
        @Override
        public Component getListCellRendererComponent(
                JList<? extends Promotion> list,
                Promotion value,
                int index,
                boolean isSelected,
                boolean cellHasFocus) {

            setText(value.getCode() + " - " + value.getDescription());
            setOpaque(true);
            setBackground(isSelected ? Color.LIGHT_GRAY : Color.WHITE);
            setForeground(Color.BLACK);
            return this;
        }
    }
}
