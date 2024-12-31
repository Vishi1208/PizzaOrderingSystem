package model;

import util.OrderState;
import util.Observer;
import util.decorators.PizzaDecorator;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private final List<Pizza> pizzas;
    private double totalPrice;
    private OrderState state;
    private final List<Observer> observers;
    private String feedback;
    private int rating;
    private final List<String> appliedPromotions;
    private String orderType;
    private String deliveryAddress;
    private int loyaltyPointsEarned;
    private final String orderId;

    public Order(String orderId) {
        this.pizzas = new ArrayList<>();
        this.totalPrice = 0.0;
        this.state = OrderState.PLACED;
        this.observers = new ArrayList<>();
        this.feedback = "";
        this.rating = 0;
        this.appliedPromotions = new ArrayList<>();
        this.orderType = "Pickup";
        this.deliveryAddress = "";
        this.loyaltyPointsEarned = 0;
        this.orderId = orderId;
    }

    public synchronized void addPizza(Pizza pizza) {
        if (pizza == null) {
            throw new IllegalArgumentException("Pizza cannot be null.");
        }
        pizzas.add(pizza);
        totalPrice += calculatePizzaPrice(pizza);
        calculateLoyaltyPoints();
        notifyObservers();
    }

    private double calculatePizzaPrice(Pizza pizza) {
        double basePrice;
        switch (pizza.getSize().toLowerCase()) {
            case "small":
                basePrice = 8.0;
                break;
            case "medium":
                basePrice = 10.0;
                break;
            case "large":
                basePrice = 12.0;
                break;
            default:
                basePrice = 10.0;
        }

        if (pizza.getCrust().equalsIgnoreCase("Stuffed")) basePrice += 2.0;
        if (pizza.getSauce().equalsIgnoreCase("Pesto")) basePrice += 1.5;
        if (pizza.getTopping().equalsIgnoreCase("Pepperoni")) basePrice += 1.5;
        if (pizza.getCheese().equalsIgnoreCase("Mozzarella")) basePrice += 2.0;

        if (pizza instanceof PizzaDecorator) {
            return ((PizzaDecorator) pizza).calculatePrice(basePrice);
        }

        return basePrice;
    }

    public synchronized double calculateTotal() {
        totalPrice = 0.0;
        for (Pizza pizza : pizzas) {
            totalPrice += calculatePizzaPrice(pizza);
        }
        calculateLoyaltyPoints();
        notifyObservers();
        return totalPrice;
    }

    private void calculateLoyaltyPoints() {
        this.loyaltyPointsEarned = (int) (totalPrice / 10);
    }

    public int getLoyaltyPointsEarned() {
        return loyaltyPointsEarned;
    }

    public synchronized double applyDiscount(double discountPercentage, String promotionName) {
        if (promotionName == null || promotionName.isEmpty() || appliedPromotions.contains(promotionName)) {
            throw new IllegalArgumentException("Invalid or duplicate promotion name.");
        }

        double discountAmount = totalPrice * (discountPercentage / 100);
        totalPrice -= discountAmount;
        appliedPromotions.add(promotionName);
        calculateLoyaltyPoints();
        notifyObservers();
        return discountAmount;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public List<Pizza> getPizzas() {
        return new ArrayList<>(pizzas);
    }

    public synchronized void addObserver(Observer observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public synchronized void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(state);
        }
    }

    public synchronized void updateState(OrderState newState) {
        if (this.state != newState) {
            this.state = newState;
            notifyObservers();
        }
    }

    public OrderState getState() {
        return state;
    }

    public String getStateDescription() {
        switch (state) {
            case PLACED:
                return "Your order has been placed!";
            case PREPARING:
                return "Your pizza is being prepared!";
            case OUT_FOR_DELIVERY:
                return "Your order is out for delivery!";
            case COMPLETED:
                return "Your order has been delivered!";
            default:
                return "Unknown state!";
        }
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setRating(int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }
        this.rating = rating;
    }

    public int getRating() {
        return rating;
    }

    public List<String> getAppliedPromotions() {
        return new ArrayList<>(appliedPromotions);
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public String getOrderId() {
        return orderId;
    }

    public String displayOrderSummary() {
        StringBuilder summary = new StringBuilder("Order Summary:\n");
        summary.append("Order ID: ").append(orderId).append("\n");
        for (Pizza pizza : pizzas) {
            summary.append("- ").append(pizza.getDescription()).append("\n");
        }
        summary.append("Total Price: $").append(String.format("%.2f", totalPrice)).append("\n");
        summary.append("Loyalty Points Earned: ").append(loyaltyPointsEarned).append("\n");
        summary.append("Order Type: ").append(orderType).append("\n");
        if (orderType.equalsIgnoreCase("Delivery")) {
            summary.append("Delivery Address: ").append(deliveryAddress).append("\n");
        }
        summary.append("Order Status: ").append(getStateDescription()).append("\n");
        summary.append("Rating: ").append(rating == 0 ? "Not Rated" : rating).append("\n");
        summary.append("Feedback: ").append(feedback.isEmpty() ? "No Feedback Provided" : feedback).append("\n");

        if (!appliedPromotions.isEmpty()) {
            summary.append("Applied Promotions: ").append(String.join(", ", appliedPromotions)).append("\n");
        }

        return summary.toString();
    }
}
