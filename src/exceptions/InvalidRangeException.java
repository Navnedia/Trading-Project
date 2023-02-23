package exceptions;

public class InvalidRangeException extends DataValidationException {
    public InvalidRangeException(String msg) { super(msg); }
     public InvalidRangeException(String msg, Throwable cause) { super(msg, cause); }
}
