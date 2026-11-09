package pas.v2026.pasrest.exceptions;

import java.util.UUID;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(UUID id) {
        super(String.format("Użytkownik o identyfikatorze %s nie został znaleziony.", id));
    }
    
    public UserNotFoundException(String login) {
        super(String.format("Użytkownik o loginie '%s' nie został znaleziony.", login));
    }
}
