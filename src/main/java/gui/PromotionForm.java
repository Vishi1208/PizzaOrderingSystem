package gui;

import model.Promotion;

import javax.swing.*;
import java.awt.*;

public class PromotionForm {
    public static Promotion showForm(Promotion promotion) {
        JTextField codeField = new JTextField(promotion != null ? promotion.getCode() : "");
        JTextField discountField = new JTextField(promotion != null ? String.valueOf(promotion.getDiscount()) : "");
        JTextField descriptionField = new JTextField(promotion != null ? promotion.getDescription() : "");

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Promotion Code:"));
        panel.add(codeField);
        panel.add(new JLabel("Discount:"));
        panel.add(discountField);
        panel.add(new JLabel("Description:"));
        panel.add(descriptionField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Promotion Form",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String code = codeField.getText().trim();
            double discount;
            try {
                discount = Double.parseDouble(discountField.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid discount value.", "Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }
            String description = descriptionField.getText().trim();
            return new Promotion(code, discount, description);
        }
        return null;
    }
}
