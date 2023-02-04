package exceptions;

public class InvalidOrderOperation extends TradingApplicationException {
    public InvalidOrderOperation(String msg) { super(msg); }
     public InvalidOrderOperation(String msg, Throwable cause) { super(msg, cause); }
}
