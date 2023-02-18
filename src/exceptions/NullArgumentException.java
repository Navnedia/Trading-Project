package exceptions;

public class NullArgumentException extends TradingApplicationException {
    public NullArgumentException(String msg) { super(msg); }
     public NullArgumentException(String msg, Throwable cause) { super(msg, cause); }
}
