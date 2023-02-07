package exceptions;

public class InvalidRangeException extends TradingApplicationException {
    public InvalidRangeException(String msg) { super(msg); }
     public InvalidRangeException(String msg, Throwable cause) { super(msg, cause); }
}
