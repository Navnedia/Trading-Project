package exceptions;

public class TradingApplicationException extends Exception {
    public TradingApplicationException(String msg) { super(msg); }
    public TradingApplicationException(String msg, Throwable cause) { super(msg, cause); }
}
