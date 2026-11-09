package pas.v2026.pasrest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnknownUserTypeException extends BusinessException {
    public UnknownUserTypeException() {
        super("Nieznany typ użytkownika.");
    }

    public UnknownUserTypeException(String message) {
        super(message);
    }
}
