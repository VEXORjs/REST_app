package pas.v2026.pasrest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidDateRangeException extends BusinessException {
    public InvalidDateRangeException() {
        super("Nieprawidłowy przedział czasu. Czas zakończenia musi być po czasie rozpoczęcia.");
    }

    public InvalidDateRangeException(String message) {
        super(message);
    }
}
