package pas.v2026.pasrest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public abstract class ConflictException extends BusinessException {
    public ConflictException(String message)
    {
        super(message);
    }
}
