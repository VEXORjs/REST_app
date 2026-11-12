package pas.v2026.pasrest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class InActiveUserException  extends BusinessException {
    public InActiveUserException(String userId) {
        super(String.format("Klient %s jest nieaktywny. Nie można dokonać alokacji.", userId));
    }
}
