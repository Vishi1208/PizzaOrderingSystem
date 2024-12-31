
package util.commands;

import model.Order;

public class PlaceOrderCommand implements command {
    private Order order;

    public PlaceOrderCommand(Order order) {
        this.order = order;
    }

    @Override
    public void execute() {
        System.out.println("Placing order...");
        System.out.println(order.displayOrderSummary());
        order.updateState(util.OrderState.PLACED);
    }
}
