package pas.v2026.pasrest.exceptions;

import java.util.UUID;

public class ResourceHasActiveAllocationsException extends ConflictException{
    public ResourceHasActiveAllocationsException(UUID id) {
        super(String.format("Nie można usunąć zasobu %s. Posiada on powiązane alokacje.", id));
    }
}
