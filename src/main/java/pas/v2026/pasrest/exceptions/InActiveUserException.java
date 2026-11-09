package pas.v2026.pasrest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class InActiveUserException  extends BusinessException {
    public InActiveUserException(UUID userId) {
        super(String.format("Klient %s jest nieaktywny. Nie można dokonać alokacji.", userId));
    }
}
