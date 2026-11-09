package pas.v2026.pasrest.exceptions;

import java.util.UUID;

public class AllocationNotFoundException extends NotFoundException {
    public AllocationNotFoundException(UUID id) {
      super(String.format("Alokacja o identyfikatorze %s nie została znaleziona", id));
    }
}
