package pas.v2026.pasrest.exceptions;

public abstract class BusinessException extends RuntimeException{
    public BusinessException(String message) {
        super(message);
    }
}
