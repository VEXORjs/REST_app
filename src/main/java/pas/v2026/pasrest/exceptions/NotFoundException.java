package pas.v2026.pasrest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND)
public abstract class NotFoundException extends BusinessException {
    public NotFoundException(String message) {
        super(message);
    }
}

