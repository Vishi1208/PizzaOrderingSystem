package model;

public class Pizza {
    private String name;
    private String crust;
    private String sauce;
    private String topping;
    private String cheese;
    private String size;
    private double price;

    public Pizza(String crust, String sauce, String topping, String cheese) {
        this.name = "Custom Pizza";
        this.crust = crust;
        this.sauce = sauce;
        this.topping = topping;
        this.cheese = cheese;
        this.size = "Medium";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name;
        }
    }

    public String getCrust() {
        return crust;
    }

    public String getSauce() {
        return sauce;
    }

    public String getTopping() {
        return topping;
    }

    public String getCheese() {
        return cheese;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return String.format("Name: %s | Size: %s | Crust: %s | Sauce: %s | Topping: %s | Cheese: %s | Price: $%.2f",
                name, size, crust, sauce, topping, cheese, price);
    }
}
