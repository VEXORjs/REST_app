package pas.v2026.pasrest.exceptions;

import java.util.UUID;

public class ResourceAlreadyAllocatedException extends ConflictException{
    public ResourceAlreadyAllocatedException(UUID resourceId) {
        super(String.format("Zasób %s jest już zaalokowany przez kogoś innego w tym przedziale czasu.", resourceId));
    }
}
