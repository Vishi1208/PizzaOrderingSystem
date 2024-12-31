package model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class OrderManager {
    private static OrderManager instance;
    private final Map<String, Order> orders;
    private final Map<User, String> userOrders;
    private final Map<String, Consumer<Void>> observers;

    private OrderManager() {
        orders = new HashMap<>();
        userOrders = new HashMap<>();
        observers = new HashMap<>();
    }

    public static synchronized OrderManager getInstance() {
        if (instance == null) {
            instance = new OrderManager();
        }
        return instance;
    }

    public synchronized Order createNewOrderForUser(User user) {
        String orderId = generateOrderId();
        Order newOrder = new Order(orderId);
        orders.put(orderId, newOrder);
        userOrders.put(user, orderId);
        notifyObservers();
        return newOrder;
    }

    public synchronized Order getOrderForUser(User user) {
        String orderId = userOrders.get(user);
        if (orderId != null) {
            return orders.get(orderId);
        }
        return null;
    }

    public synchronized Map<String, Order> getAllOrders() {
        return Collections.unmodifiableMap(orders);
    }

    public synchronized Order getOrder(String orderId) {
        return orders.get(orderId);
    }

    public synchronized void addOrderObserver(String observerId, Consumer<Void> observer) {
        observers.put(observerId, observer);
    }

    public synchronized void removeOrderObserver(String observerId) {
        observers.remove(observerId);
    }

    private synchronized void notifyObservers() {
        for (Consumer<Void> observer : observers.values()) {
            observer.accept(null);
        }
    }

    private synchronized String generateOrderId() {
        return "ORD-" + (orders.size() + 1);
    }

    public synchronized void addOrder(Order order) {
        if (!orders.containsKey(order.getOrderId())) {
            orders.put(order.getOrderId(), order);
            notifyObservers();
        }
    }

    public synchronized void updateOrder(Order order) {
        if (orders.containsKey(order.getOrderId())) {
            orders.put(order.getOrderId(), order);
            notifyObservers();
        }
    }
}
