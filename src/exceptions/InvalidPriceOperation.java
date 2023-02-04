package exceptions;

public class InvalidPriceOperation extends TradingApplicationException {
    public InvalidPriceOperation(String msg) { super(msg); }
    public InvalidPriceOperation(String msg, Throwable cause) { super(msg, cause); }
}
