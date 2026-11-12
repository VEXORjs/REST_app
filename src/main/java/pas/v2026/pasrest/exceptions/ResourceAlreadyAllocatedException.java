package pas.v2026.pasrest.exceptions;

public class ResourceAlreadyAllocatedException extends ConflictException{
    public ResourceAlreadyAllocatedException(String resourceId) {
        super(String.format("Zasób %s jest już zaalokowany przez kogoś innego w tym przedziale czasu.", resourceId));
    }
}
