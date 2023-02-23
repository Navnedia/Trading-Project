package exceptions;

public class InvalidOperationException extends TradingApplicationException {
    public InvalidOperationException(String msg) { super(msg); }
    public InvalidOperationException(String msg, Throwable cause) { super(msg, cause); }
}
