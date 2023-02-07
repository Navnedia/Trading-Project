package exceptions;

public class InvalidArgumentException extends TradingApplicationException {
    public InvalidArgumentException(String msg) { super(msg); }
     public InvalidArgumentException(String msg, Throwable cause) { super(msg, cause); }
}
