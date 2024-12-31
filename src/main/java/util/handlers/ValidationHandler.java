package util.handlers;

public abstract class ValidationHandler {
    private ValidationHandler nextHandler;
    public void setNextHandler(ValidationHandler nextHandler) {
        this.nextHandler = nextHandler;
    }


    public abstract boolean handle(String input);

    protected boolean handleNext(String input) {
        if (nextHandler != null) {
            return nextHandler.handle(input);
        }
        return true;
    }
}
