package pas.v2026.pasrest.exceptions;

import java.util.UUID;


public class ResourceNotFoundException extends NotFoundException {
    public ResourceNotFoundException(UUID id) {
        super(String.format("Zasób o identyfikatorze %s nie został znaleziony", id));
    }
}
