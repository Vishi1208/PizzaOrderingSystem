package gui;

import model.Order;
import model.User;

public class Main {
    public static void main(String[] args) {
        User user = new User("John Doe", "john.doe@example.com", "123-456-7890");
        Order order = new Order("Order-" + System.currentTimeMillis());
        new DashboardGUI(order, user);
    }
}
