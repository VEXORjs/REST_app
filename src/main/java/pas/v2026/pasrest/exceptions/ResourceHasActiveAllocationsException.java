package pas.v2026.pasrest.exceptions;

public class ResourceHasActiveAllocationsException extends ConflictException{
    public ResourceHasActiveAllocationsException(String id) {
        super(String.format("Nie można usunąć zasobu %s. Posiada on powiązane alokacje.", id));
    }
}
