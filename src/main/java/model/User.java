package model;

import model.Pizza;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private String name;
    private String email;
    private String phone;
    private int loyaltyPoints;
    private List<Pizza> favoritePizzas;

    // Constructor
    public User(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.loyaltyPoints = 0;
        this.favoritePizzas = new ArrayList<>();
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    // Add loyalty points
    public void addLoyaltyPoints(int points) {
        if (points > 0) {
            this.loyaltyPoints += points;
        } else {
            throw new IllegalArgumentException("Points to add must be greater than 0.");
        }
    }

    // Subtract loyalty points
    public boolean subtractLoyaltyPoints(int points) {
        if (points > 0 && loyaltyPoints >= points) {
            this.loyaltyPoints -= points;
            return true;
        }
        return false;
    }

    public double redeemPointsForDiscount(int points) {
        if (subtractLoyaltyPoints(points)) {
            return points / 10.0;
        }
        throw new IllegalArgumentException("Insufficient loyalty points to redeem.");
    }

    public List<Pizza> getFavoritePizzas() {
        return new ArrayList<>(favoritePizzas);
    }

    public void addFavoritePizza(Pizza pizza) {
        if (pizza == null) {
            throw new IllegalArgumentException("Pizza cannot be null.");
        }
        if (!isFavoritePizza(pizza)) {
            favoritePizzas.add(pizza);
        } else {
            System.out.println("Pizza is already in the favorites list.");
        }
    }

    public void removeFavoritePizza(Pizza pizza) {
        if (!favoritePizzas.remove(pizza)) {
            throw new IllegalArgumentException("Pizza not found in favorites.");
        }
    }

    public Pizza reorderFavorite(int index) {
        if (index >= 0 && index < favoritePizzas.size()) {
            return favoritePizzas.get(index);
        }
        throw new IndexOutOfBoundsException("Invalid favorite pizza index.");
    }

    public String displayFavorites() {
        if (favoritePizzas.isEmpty()) {
            return "No favorite pizzas saved.";
        }
        StringBuilder builder = new StringBuilder("Favorite Pizzas:\n");
        for (int i = 0; i < favoritePizzas.size(); i++) {
            builder.append(i + 1).append(". ").append(favoritePizzas.get(i).getDescription()).append("\n");
        }
        return builder.toString();
    }

    public boolean isFavoritePizza(Pizza pizza) {
        return favoritePizzas.stream()
                .anyMatch(favorite -> favorite.getDescription().equalsIgnoreCase(pizza.getDescription()));
    }

    public String displayUserSummary() {
        return String.format(
                "User: %s\nEmail: %s\nPhone: %s\nLoyalty Points: %d\n",
                name, email, phone, loyaltyPoints
        );
    }
}
