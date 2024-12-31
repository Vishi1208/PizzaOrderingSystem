
package util.commands;

import model.Order;

public class SaveFeedbackCommand implements command {
    private Order order;
    private int rating;
    private String feedback;

    public SaveFeedbackCommand(Order order, int rating, String feedback) {
        this.order = order;
        this.rating = rating;
        this.feedback = feedback;
    }

    @Override
    public void execute() {
        order.setRating(rating);
        order.setFeedback(feedback);
        System.out.println("Feedback saved: " + feedback);
        System.out.println("Rating: " + rating);
    }
}
