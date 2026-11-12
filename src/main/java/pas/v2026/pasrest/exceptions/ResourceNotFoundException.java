package pas.v2026.pasrest.exceptions;


public class ResourceNotFoundException extends NotFoundException {
    public ResourceNotFoundException(String id) {
        super(String.format("Zasób o identyfikatorze %s nie został znaleziony", id));
    }
}
