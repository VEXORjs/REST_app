package pas.v2026.pasrest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class InvalidUserRoleException extends BusinessException {
    
    public InvalidUserRoleException() {
        super("Wskazany użytkownik nie jest Klientem. Tylko Klienci mogą posiadać alokacje.");
    }

    public InvalidUserRoleException(String message) {
        super(message);
    }
}
