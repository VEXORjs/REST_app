package pas.v2026.pasrest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnknownResourceTypeException extends BusinessException {
    public UnknownResourceTypeException() {
        super("Nieznany typ zasobu.");
    }

    public UnknownResourceTypeException(String message) {
        super(message);
    }
}
