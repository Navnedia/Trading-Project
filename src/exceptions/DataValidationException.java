package exceptions;

public class DataValidationException extends TradingApplicationException{
    public DataValidationException(String msg) { super(msg); }
    public DataValidationException(String msg, Throwable cause) { super(msg, cause); }
}
